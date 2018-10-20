package net.robotics.sensor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import net.robotics.main.Robot;
import net.robotics.screen.LCDRenderer;

public class UltrasonicSensorMonitor extends Thread{
	
	private EV3UltrasonicSensor ultrasonicSensor;
	
	private Robot robot;
	private int Delay;
	
	public static enum ColorNames{
		GREEN,
		WHITE,
		BLACK,
		BLUE,
		YELLOW,
		RED,
		UNKNOWN
	}
	
	private static final float[][] ColorRanges = {
			{0.15f},
			{0.15f},
			{0.05f},
			{0.08f},
			{0.05f, 0.05f, 0.1f},
			{0.05f, 0.05f, 0.1f}
	};
	
	public UltrasonicSensorMonitor(Robot robot, EV3UltrasonicSensor sensor, EV3MediumRegulatedMotor motor, int Delay){
		this.setDaemon(true);
		this.robot = robot;
		this.Delay = Delay;
	}
	
	public synchronized void start() {
		super.start();
	}



	public void configure(){
	}
	
	public void run() {
		
		
		while(true){
			try{
				sleep(Delay);
			} catch(Exception e){
				
			}
		}
	}
}
