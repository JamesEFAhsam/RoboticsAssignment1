package net.robotics.sensor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.sensor.EV3ColorSensor;
import net.robotics.main.Robot;
import net.robotics.screen.LCDRenderer;

public class ColorSensorMonitor extends Thread{

	private static HashMap<ColorNames, RGBFloat> ColorKeys = new HashMap<>();
	private EV3ColorSensor colorSensor;
	private float[] colorSample;

	private Robot robot;
	private int Delay;

	private LinkedList<ColorNames> pastStrings;
	private ColorNames currentColor;
	private HashMap<ColorNames, Integer> frequency;
	
	private static final int MeasurementAmount = 16;

	public static enum ColorNames{
		//GREEN,

		BLUE,
		BLACK,
		WHITE,
		//YELLOW,
		//RED,
		UNKNOWN
	}

	private static final float[][] ColorRanges = {
			//{0.15f},
			{0.005f, 0.005f, 0.01f}, //BLUE Colors
			{0.2f},
			{0.2f}
			//{0.05f, 0.05f, 0.1f},
			//{0.05f, 0.05f, 0.1f}
	};

	public ColorSensorMonitor(Robot robot, EV3ColorSensor sensor, int Delay){
		this.setDaemon(true);
		this.frequency = new HashMap<>();
		this.colorSensor = sensor;
		this.colorSample = new float[this.colorSensor.getRGBMode().sampleSize()];
		this.pastStrings = new LinkedList<>();
		this.robot = robot;
		this.Delay = Delay;
	}

	public synchronized void start() {
		for (ColorNames color : ColorNames.values()) {
			frequency.put(color, 0);
		}

		robot.screen.writeTo(new String[]{
				"Starting Thread...",
		}, robot.screen.getWidth()/2, 0, GraphicsLCD.HCENTER, true);
		super.start();
	}



	public void configure(boolean loadFromFile){
		int i = 0;

		Gson gson = new Gson();

		for (ColorNames colorname : ColorNames.values()) {
			if(colorname == ColorNames.UNKNOWN){
				continue;
			}

			if(!loadFromFile){
				float[] range = ColorRanges[i];
				i++;

				robot.screen.writeTo(new String[]{
						"Waiting for " + colorname,
						"Press any button",

				}, robot.screen.getWidth()/2, 0, GraphicsLCD.HCENTER, true);

				robot.screen.drawEnterButton("Next", robot.screen.getWidth()-30-2, robot.screen.getHeight()-30-2, 30, 30);

				robot.screen.drawDownButton("Skip", robot.screen.getWidth()-80-2, robot.screen.getHeight()-30-2, 45, 30);

				robot.screen.drawEscapeButton("Quit", 0, robot.screen.getHeight()-45/2-2, 45, 45/2, 6);

				Button.waitForAnyPress();

				if(Button.ESCAPE.isDown()){
					robot.closeProgram();
					Button.waitForAnyPress();
				}

				if(Button.DOWN.isDown())
					continue;

				robot.screen.clearScreen();

				float[] rgb = getRGB();
				float[] high, low, avgrange;

				high = new float[3];
				low = new float[3];

				avgrange = new float[3];

				avgrange[0] = range[0] /2f;

				if(range.length > 1){
					avgrange[1] = range[1] /2f;
					if(range.length > 2){
						avgrange[2] = range[2] /2f;
					} else {
						avgrange[2] = range[1] /2f;
					}
				} else {
					avgrange[1] = range[0] /2f;
					avgrange[2] = range[0] /2f;
				}

				for (int j = 0; j < rgb.length; j++) {
					float mid = rgb[j];
					high[j] = mid + avgrange[j];
					low[j] = mid - avgrange[j];
				}



				robot.screen.writeTo(new String[]{
						"Reading: " + colorname,
						"R("+rgb[0]+")",
						"G("+rgb[1]+")",
						"B("+rgb[2]+")",
						"RA("+avgrange[0]+")",
						"GA("+avgrange[1]+")",
						"BA("+avgrange[2]+")",
				}, robot.screen.getWidth()/2, 0, GraphicsLCD.HCENTER, true);

				ColorKeys.put(colorname, new RGBFloat(low, high));

				robot.screen.drawEnterButton("Next", robot.screen.getWidth()-30-2, robot.screen.getHeight()-30-2, 30, 30);

				robot.screen.drawEscapeButton("Quit", 0, robot.screen.getHeight()-45/2-2, 45, 45/2, 6);

				Button.waitForAnyPress();

				for (int j = 0; j < rgb.length; j++) {
					float mid = rgb[j];
					high[j] = mid + avgrange[j];
					low[j] = mid - avgrange[j];
				}

				ColorKeys.put(colorname, new RGBFloat(low, high));

				try (PrintWriter out = new PrintWriter(colorname.name()+".json")) {
					out.println(gson.toJson(ColorKeys.get(colorname)));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				robot.screen.drawEnterButton("Next", robot.screen.getWidth()-30-2, robot.screen.getHeight()-30-2, 30, 30);

				robot.screen.drawEscapeButton("Quit", 0, robot.screen.getHeight()-45/2-2, 45, 45/2, 6);

				do{
					Button.waitForAnyPress();

					if(Button.ESCAPE.isDown())
						robot.closeProgram();

				} while(!Button.ENTER.isDown());
			} else {
				try {
					ColorKeys.put(colorname, gson.fromJson(new String(Files.readAllBytes(Paths.get(colorname.name()+".json"))), RGBFloat.class));
				} catch (JsonSyntaxException | IOException e) {
					e.printStackTrace();
				}
			}
		}

		robot.screen.clearScreen();
	}

	public void run() {


		while(true){
			if(pastStrings.size() < MeasurementAmount){
				currentColor = ColorNames.UNKNOWN;

				ColorNames key = getColor();
				if(key == ColorNames.UNKNOWN){
					continue;
				}

				/*screen.writeTo(new String[]{
						"Color " + (key),
				}, screen.getWidth()/2, 0, GraphicsLCD.HCENTER, true);*/

				if(frequency.containsKey(key)){
					pastStrings.push(key);
					int modified = frequency.get(key);
					if(key == ColorNames.BLACK){
						modified+=2;
					}else{
						modified++;
					}
					frequency.put(key, modified);
				}
			} else {
				ColorNames key = getColor();
				if(key == ColorNames.UNKNOWN){
					continue;
				}
				pastStrings.push(key);
				int modified = frequency.get(key);
				if(key == ColorNames.BLACK){
					modified+=2;
				}else{
					modified++;
				}
				frequency.put(key, modified);

				key = pastStrings.pollLast();
				modified = frequency.get(key);
				if(key == ColorNames.BLACK){
					modified-=2;
				}else{
					modified--;
				}
				frequency.put(key, modified);


				ColorNames color = ColorNames.UNKNOWN;
				int f = 0;
				for (ColorNames colorNames : frequency.keySet()) {
					int value = frequency.get(colorNames);
					if(f < value){
						color = colorNames;
						f = value;
					}
				}

				currentColor = color;
			}

			try{
				sleep(Delay);
			} catch(Exception e){

			}
		}
	}

	public float getRedColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		return colorSample[0];
	}

	public float getGreenColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		return colorSample[1];
	}

	public float getBlueColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		return colorSample[2];
	}

	public String getRGBColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		return "R(" + colorSample[0] + "), G(" + colorSample[1] + ") B(" + colorSample[2] + ")";
	}

	public float[] getRGB(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		return colorSample;
	}

	public ColorNames getCurrentColor(){
		return currentColor;
	}

	public HashMap<ColorNames, Integer> getColorFrequency(){
		return frequency;
	}
	
	public static RGBFloat getColorRanges(ColorNames name){
		return ColorKeys.get(name);
	}

	public ColorNames getColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		RGBFloat color = new RGBFloat(colorSample[0], colorSample[1], colorSample[2]);

		for (ColorNames key : ColorKeys.keySet()) {
			if(key == ColorNames.UNKNOWN){
				continue;
			}

			RGBFloat comp = ColorKeys.get(key);
			if(comp.Compare(color))
				return key;
		}

		return ColorNames.UNKNOWN;
	}
}
