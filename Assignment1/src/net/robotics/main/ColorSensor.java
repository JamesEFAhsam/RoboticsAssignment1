package net.robotics.main;

import java.util.Dictionary;
import java.util.HashMap;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.internal.ev3.EV3LED;

public class ColorSensor {
	
	
	
	/*private static RGBFloat GREEN = new RGBFloat(new RGBFloat(0f, 0.19f, 0f), new RGBFloat(0.15f, 0.25f, 0.15f));
	private static RGBFloat WHITE = new RGBFloat(new RGBFloat(0.22f, 0.22f, 0.22f), new RGBFloat(0.3f, 0.3f, 0.3f));
	private static RGBFloat BLACK = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.00f), new RGBFloat(0.05f, 0.05f, 0.05f));
	private static RGBFloat BLUE = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.05f), new RGBFloat(0.075f, 0.055f, 0.1f));
	private static RGBFloat YELLOW = new RGBFloat(new RGBFloat(0.25f, 0.15f, 0.00f), new RGBFloat(0.30f, 0.20f, 0.1f));
	private static RGBFloat RED = new RGBFloat(new RGBFloat(0.20f, 0.00f, 0.00f), new RGBFloat(0.25f, 0.05f, 0.075f));*/
	
	private LCDRenderer screen;
	private ColorSensorMonitor colorSensor;
	EV3LED led;
	
	public static void main(String[] args){
		ColorSensor cs = new ColorSensor();
		
		cs.mainLoop();
		
		cs.closeRobot();
	}
	
	public ColorSensor() {
		Brick myEV3 = BrickFinder.getDefault();
		
		led = (EV3LED) myEV3.getLED();
		
		screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());
		
		colorSensor = new ColorSensorMonitor(this, new EV3ColorSensor(myEV3.getPort("S2")), 10);
				
		//screen.writeTo(new String[]{"Alive? "+(colorSensor != null)});

		colorSensor.configure();
		colorSensor.start();
		
		
	}
	
	public void closeProgram(){
		closeRobot();
		System.exit(0);
	}
	
	public void mainLoop(){
		while(!Button.ESCAPE.isDown()){
			
			//screen.clearScreen();
			//screen.writeTo(new String[]{"F? "+(colorSensor.getColorFrequency() != null)});
			
			
			
			Button.waitForAnyPress();
		}
	}
	
	public LCDRenderer getScreen(){
		return screen;
	}
	
	public void closeRobot(){
	}
}
