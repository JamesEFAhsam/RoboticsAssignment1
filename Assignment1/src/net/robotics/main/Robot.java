package net.robotics.main;

import java.util.Dictionary;
import java.util.HashMap;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.internal.ev3.EV3LED;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.SampleProvider;
import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.screen.LCDRenderer;
import net.robotics.sensor.ColorSensorMonitor;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;
import net.robotics.sensor.UltrasonicSensorMonitor;

public class Robot {



	/*private static RGBFloat GREEN = new RGBFloat(new RGBFloat(0f, 0.19f, 0f), new RGBFloat(0.15f, 0.25f, 0.15f));
	private static RGBFloat WHITE = new RGBFloat(new RGBFloat(0.22f, 0.22f, 0.22f), new RGBFloat(0.3f, 0.3f, 0.3f));
	private static RGBFloat BLACK = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.00f), new RGBFloat(0.05f, 0.05f, 0.05f));
	private static RGBFloat BLUE = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.05f), new RGBFloat(0.075f, 0.055f, 0.1f));
	private static RGBFloat YELLOW = new RGBFloat(new RGBFloat(0.25f, 0.15f, 0.00f), new RGBFloat(0.30f, 0.20f, 0.1f));
	private static RGBFloat RED = new RGBFloat(new RGBFloat(0.20f, 0.00f, 0.00f), new RGBFloat(0.25f, 0.05f, 0.075f));*/

	public LCDRenderer screen;
	private ColorSensorMonitor colorSensor;
	private UltrasonicSensorMonitor ultrasonicSensor;
	private EV3LED led;
	private MovePilot pilot;
	private OdometryPoseProvider opp;
	private Map map;
	
	public final float _OCCUPIEDBELIEFCUTOFF = 0.75f;
	
	private static Port leftBumpPort = LocalEV3.get().getPort("S1");
	private static Port rightBumpPort = LocalEV3.get().getPort("S4");
	
	private static EV3TouchSensor leftBumper = new EV3TouchSensor(leftBumpPort);
	private static EV3TouchSensor rightBumper = new EV3TouchSensor(rightBumpPort);
	
	private static SampleProvider leftTouch = leftBumper.getMode("Touch");
	private static SampleProvider rightTouch = rightBumper.getMode("Touch");
	
	private static float[] leftSample = new float[leftTouch.sampleSize()];
	private static float[] rightSample = new float[rightTouch.sampleSize()];
	
	public static Robot current;
	
	

	public static void main(String[] args){
		current = new Robot();

		current.mainLoop();

		current.closeRobot();
	}

	public Robot() {
		Brick myEV3 = BrickFinder.getDefault();

		led = (EV3LED) myEV3.getLED();

		screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());

		colorSensor = new ColorSensorMonitor(this, new EV3ColorSensor(myEV3.getPort("S2")), 16);
		
		NXTRegulatedMotor motor = null;
		EV3UltrasonicSensor ultra = null;
		
		while(true){
			try {
				ultra = new EV3UltrasonicSensor(myEV3.getPort("S3"));
				motor = Motor.C;
				break;
			} catch(Exception e) {
			}
		}
		
		
		ultrasonicSensor = new UltrasonicSensorMonitor(this, 
				ultra, 
				motor, 60);
		
		setUpRobot();

		//screen.writeTo(new String[]{"Alive? "+(colorSensor != null)});

		this.map = new Map(6, 7);

		colorSensor.configure(true);
		colorSensor.start();
		
		ultrasonicSensor.start();


	}

	private void setUpRobot(){
		pilot = ChasConfig.getPilot();

		// Create a pose provider and link it to the move pilot
		opp = new OdometryPoseProvider(pilot);
	}
	
	
	

	public void closeProgram(){
		closeRobot();
		System.exit(0);
	}

	public void mainLoop(){
		int squares = 0;
		/*
		ColorNames prevColor = colorSensor.getColor();
		
		int heading = 0; // 0 Forward, Right, Back, Left
		int amount = 0;
		boolean visitOverride = false; 

		pilot.setLinearSpeed(10);

		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, 8, map);
		*/
		Localisation localisation = new Localisation();
		
		map.setRobotPos(3, 4, 3);
		map.getTile(3, 5).view(false);
		
		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);

		
		localisation.localiseOrientation();

		

		 while(!Button.ESCAPE.isDown() && squares < 6 ){
			 /*
			screen.clearScreen();
			
			
			
			screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
			
			
			if((!map.beenVisited(heading) || visitOverride) && map.canMove(heading)){
				
				observe(heading);
				ultrasonicSensor.resetMotor();	
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				screen.writeTo(new String[]{
						"V: " + visitOverride
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
				
				

				MoveSquares(1);
				
				map.moveRobotPos(heading);
				
				observe(heading);
				ultrasonicSensor.resetMotor();	
				
				screen.clearScreen();
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				/*screen.writeTo(new String[]{
						"F: " + F,
						"L: " + L,
						"R: " + R
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
				
				visitOverride = false;
				amount = 0;
				squares++;
				
			} else {
				
				pilot.rotate(90);

				observe(heading);
				ultrasonicSensor.resetMotor();	

				heading++;
				if(heading > 3)
					heading = 0;

				amount++;
				if(amount >= 4){
					amount = 0;
					visitOverride = true;
				}
			}
			
			  */
			

			//screen.clearScreen();
			//screen.writeTo(new String[]{"F? "+(colorSensor.getColorFrequency() != null)});



			/*if(colorSensor.getCurrentColor() == ColorNames.BLACK && prevColor != ColorNames.BLACK){
				squares++;
			}

			if(colorSensor.getCurrentColor() != prevColor){
				prevColor = colorSensor.getCurrentColor();
			}

			screen.clearScreen();
			screen.writeTo(new String[]{"Passed through "+squares+" squares.",
					"Color. " + colorSensor.getCurrentColor(),
					"Previous. " + prevColor
					}, screen.getWidth()/2, 0, GraphicsLCD.HCENTER, Font.getSmallFont());
			screen.drawEscapeButton("QUIT", 0, 100, 45, 45/2, 6);
			*/

			Button.waitForAnyPress();
			//Button.waitForAnyPress();
		}
	}
	
	private void observe(int heading){
		ultrasonicSensor.clear();
		
		//ultrasonicSensor.getAssuredDistance();
		/*
		Robot.current.screen.writeTo(new String[]{
				"P: " + ultrasonicSensor.pointer + "/" + ultrasonicSensor.distance[ultrasonicSensor.pointer]
		}, 0, 100, GraphicsLCD.LEFT, Font.getDefaultFont());*/
		
		screen.writeTo(new String[]{
				"F: " + ultrasonicSensor.getAssuredDistance()
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		ultrasonicSensor.clear();
		try {
			Thread.sleep(60*5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float F = ultrasonicSensor.getDistance();
		ultrasonicSensor.clear();
		try {
			Thread.sleep(60*5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float L = ultrasonicSensor.rotate(90).getDistance();
		ultrasonicSensor.clear();
		try {
			Thread.sleep(60*5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float R = ultrasonicSensor.rotate(-180).getDistance();
		
		/*screen.writeTo(new String[]{
				"F: " + F,
				"L: " + L,
				"R: " + R,
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());/*/
		
		map.updateMap(heading, F, L, R);
	}

	private void MoveSquares(int i){
		int direction = (i/Math.abs(i));
		
		for (int j = 0; j < i; j++) {
			if(direction == 1)
				pilot.forward();
			else
				pilot.backward();
			ColorNames cn;
			do{
				cn = colorSensor.getCurrentColor();
			}while(cn != ColorNames.BLACK);
			pilot.travel(12.5f);
		}
	}
	
	public void turnToHeading(int desiredHeading) {
		int initialHeading = map.getRobotHeading();
		int headingDifference = desiredHeading - initialHeading;
		int rotationAmount = 0;
		switch(headingDifference) {
			case 1: 
			case -3:
				rotationAmount = 90;
				break;
			case 2: 
			case -2:
				rotationAmount = 180;
				break;
			case -1:
			case 3:
				rotationAmount = -90;
				break;
		}
		pilot.rotate(rotationAmount);
		map.setRobotPos(map.getRobotX(), map.getRobotY(), desiredHeading);
	}

	public LCDRenderer getScreen(){
		return screen;
	}

	public void closeRobot(){
	}
	
	public OdometryPoseProvider getOpp() {
		return opp;
	}
	
	public Map getMap() {
		return map;
	}

	public MovePilot getPilot() {
		return pilot;
	}
	
	public float[] getLeftSample() {
		return leftSample;
	}
	
	public float[] getRightSample() {
		return rightSample;
	}
	
	public SampleProvider getLeftTouch() {
		return leftTouch;
	}
	
	public SampleProvider getRightTouch() {
		return rightTouch;
	}
}
