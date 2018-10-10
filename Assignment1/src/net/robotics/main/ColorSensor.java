package net.robotics.main;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.internal.ev3.EV3LED;

public class ColorSensor {
	private static RGBFloat GREEN = new RGBFloat(new RGBFloat(0f, 0.19f, 0f), new RGBFloat(0.15f, 0.25f, 0.15f));
	private static RGBFloat WHITE = new RGBFloat(new RGBFloat(0.22f, 0.22f, 0.22f), new RGBFloat(0.3f, 0.3f, 0.3f));
	private static RGBFloat BLACK = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.00f), new RGBFloat(0.05f, 0.05f, 0.05f));
	private static RGBFloat BLUE = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.05f), new RGBFloat(0.075f, 0.055f, 0.1f));
	private static RGBFloat YELLOW = new RGBFloat(new RGBFloat(0.25f, 0.15f, 0.00f), new RGBFloat(0.30f, 0.20f, 0.1f));
	private static RGBFloat RED = new RGBFloat(new RGBFloat(0.20f, 0.00f, 0.00f), new RGBFloat(0.25f, 0.05f, 0.075f));
	
	private EV3ColorSensor colorSensor;
	float[] colorSample;
	GraphicsLCD lcd;
	EV3LED led;
	
	public static void main(String[] args){
		ColorSensor cs = new ColorSensor();
		
		cs.mainLoop();
		
		cs.closeRobot();
	}
	
	public ColorSensor() {
		Brick myEV3 = BrickFinder.getDefault();
		
		led = (EV3LED) myEV3.getLED();
		
		lcd = LocalEV3.get().getGraphicsLCD();
		
		colorSensor = new EV3ColorSensor(myEV3.getPort("S2"));
		
		colorSample = new float[colorSensor.getRGBMode().sampleSize()];
		
	}
	
	public void mainLoop(){
		while(!Button.ESCAPE.isDown()){
			lcd.clear();
			
			lcd.drawString("Reading: ", lcd.getWidth()/2, 0, GraphicsLCD.HCENTER);
			lcd.drawString("R("+getRedColor()+")", lcd.getWidth()/2, 20, GraphicsLCD.HCENTER);
			lcd.drawString("G("+getGreenColor()+")", lcd.getWidth()/2, 40, GraphicsLCD.HCENTER);
			lcd.drawString("B("+getBlueColor()+")", lcd.getWidth()/2, 60, GraphicsLCD.HCENTER);
			lcd.drawString("Color("+getColor()+")", lcd.getWidth()/2, 80, GraphicsLCD.HCENTER);
			
			Button.waitForAnyPress();
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
	
	
	public String getColor(){
		colorSensor.getRGBMode().fetchSample(colorSample, 0);
		RGBFloat color = new RGBFloat(colorSample[0], colorSample[1], colorSample[2]);
		
		
		
		if(color.Compare(BLACK)){

			led.setPattern(EV3LED.COLOR_NONE, EV3LED.PATTERN_ON);
			return "BLACK";
		}
		
		if(color.Compare(BLUE)){
			return "BLUE";
		}
		
		if(color.Compare(RED)){
			led.setPattern(EV3LED.COLOR_RED, EV3LED.PATTERN_ON);
			return "RED";
		}
		
		if(color.Compare(YELLOW)){
			led.setPattern(EV3LED.COLOR_ORANGE, EV3LED.PATTERN_ON);

			return "YELLOW";
		}
		
		
		
		if(color.Compare(GREEN)){
			led.setPattern(EV3LED.COLOR_GREEN, EV3LED.PATTERN_ON);
			return "GREEN";
		}
		
		if(color.Compare(WHITE)){
			led.setPattern(EV3LED.COLOR_ORANGE, EV3LED.PATTERN_BLINK);

			return "WHITE";
		}
		
		return "UNKNOWN";
	}
	
	public void closeRobot(){
		colorSensor.close();
	}
}
