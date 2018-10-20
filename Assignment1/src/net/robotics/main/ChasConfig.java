package net.robotics.main;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import net.robotics.screen.LCDRenderer;

public class ChasConfig {

	public static void main(String[] args) {
		MovePilot pilot = getPilot();
		
		LCDRenderer screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());
		
		screen.writeTo(new String[]{
				"Waiting..."
		}, screen.getWidth()/2, 0, GraphicsLCD.LEFT);
		
		Button.waitForAnyPress();
		
		screen.writeTo(new String[]{
				"Rotating..."
		}, screen.getWidth()/2, 0, GraphicsLCD.LEFT);
		
		pilot.rotate(90);
		
		Button.waitForAnyPress();
		
		screen.writeTo(new String[]{
				"Moving 100..."
		}, screen.getWidth()/2, 0, GraphicsLCD.LEFT);
		
		pilot.travel(100);
		
		Button.waitForAnyPress();
	}
	
	public static MovePilot getPilot(){
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 4.1).offset(-5.3);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 4.1).offset(5.3);

		Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot pilot = new MovePilot(myChassis);
		return pilot;
	}

}
