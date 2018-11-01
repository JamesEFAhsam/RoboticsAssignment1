package net.robotics.main;

import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.SampleProvider;
import net.robotics.behaviours.AStar;
import net.robotics.behaviours.Finalisation;
import net.robotics.behaviours.IceSlideBehavior;
import net.robotics.behaviours.LocaliseBehavior;
import net.robotics.map.Map;
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
	private LED led;
	private SoundMonitor audio;
	private MovePilot pilot;
	private OdometryPoseProvider opp;
	private Map map;
	private Localisation localisation;
	private CustomArbitrator arbitrator;
	
	public int overrideVisitAmount = 0;
	
	private static Port leftBumpPort = LocalEV3.get().getPort("S1");
	private static Port rightBumpPort = LocalEV3.get().getPort("S4");
	
	private static EV3TouchSensor leftBumper = new EV3TouchSensor(leftBumpPort);
	private static EV3TouchSensor rightBumper = new EV3TouchSensor(rightBumpPort);
	
	private static SampleProvider leftTouch = leftBumper.getMode("Touch");
	private static SampleProvider rightTouch = rightBumper.getMode("Touch");
	
	private static float[] leftSample = new float[leftTouch.sampleSize()];
	private static float[] rightSample = new float[rightTouch.sampleSize()];
	
	private boolean mapFinished;
	
	private LocaliseBehavior b1;
	private IceSlideBehavior b2;
	private AStar b3;
	private Finalisation b4;
	
	public static Robot current;
	


	public static void main(String[] args){
		new Robot();
		current.startRobot();
	}

	public Robot() {
		current = this;
		
		Brick myEV3 = BrickFinder.getDefault();
		led = myEV3.getLED();
		audio = new SoundMonitor(myEV3.getAudio());
		screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());
		
		screen.clearScreen();
		screen.writeTo(new String[]{
				"Booting..."
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		colorSensor = new ColorSensorMonitor(this, new EV3ColorSensor(myEV3.getPort("S2")), 16);
		localisation = new Localisation();
		
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

		//screen.writeTo(new String[]{"Alive? "+(colorSensor != null)});

		this.map = new Map(6, 7);

		colorSensor.configure(true);
		colorSensor.start();
		audio.start();
		ultrasonicSensor.start();
	}
	
	private void startRobot() {
		pilot = ChasConfig.getPilot();
		pilot.setLinearSpeed(12);
		
		// Create a pose provider and link it to the move pilot
		opp = new OdometryPoseProvider(pilot);
		
		setUpBehaviors();
		
		createKeyListeners();
		
		arbitrator.go();
		
		
	}
	
	private void setUpBehaviors() {
		b1 = new LocaliseBehavior();
		b2 = new IceSlideBehavior();
		b3 = new AStar(getMap());
		b4 = new Finalisation();
		//Behavior[] behaviors = {b3, b2, b1, b4};			// Behavior priority, where [0] is lowest priority
		Behavior[] behaviors = {b3, b2, b1};
		arbitrator = new CustomArbitrator(behaviors, false); // NEED TO ADD BEHAVIORS
		LCD.clearDisplay();
	}
	
	public void closeProgram(){
		arbitrator.stop();
		System.exit(0);
	}
	
	private void createKeyListeners(){
		Button.ESCAPE.addKeyListener(new KeyListener() {
			public void keyReleased(Key k) {
				Robot.current.closeProgram();
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(Key k) {
			}
		});
		
		
		Button.RIGHT.addKeyListener(new KeyListener() {
			public void keyReleased(Key k) {
				Robot.current.getScreen().cycleMode();
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(Key k) {
			}
		});
	}

	public void mainLoop(){
		// TO DO
		// Need to move this into IceSlide behavior. James? 
		int squares = 0;
		
		ColorNames prevColor = colorSensor.getColor();
		
		int amount = 0;
		boolean visitOverride = false; 

		pilot.setLinearSpeed(10);
		
		/*map.setRobotPos(2, 0, 0);
		//map.updateMap(3, 0.1f, 0.1f, 0.1f);
		//map.updateTiles(2, 0.1f);

		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);*/
		
		//Button.waitForAnyPress();
		
		map.setRobotPos(3, 4, 3);
		map.getTile(3, 5).view(false);
		
		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);


		Button.waitForAnyPress();

		 while(!Button.ESCAPE.isDown() && squares < 6 ){
			 
			screen.clearScreen();
			
			
			
			screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
			
			
			if((!map.beenVisited(map.getRobotHeading()) || visitOverride) && map.canMove(map.getRobotHeading())){
				
				observe(map.getRobotHeading());
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				/*screen.writeTo(new String[]{
						"V: " + visitOverride
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());*/
				
				

				MoveSquares(1);
				
				map.moveRobotPos(map.getRobotHeading());
				
				observe(map.getRobotHeading());
				
				screen.clearScreen();
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				/*screen.writeTo(new String[]{
						"F: " + F,
						"L: " + L,
						"R: " + R
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());*/
				
				visitOverride = false;
				amount = 0;
				squares++;
				
			} else {
				
				turnToHeading(map.getRobotHeading()+1);

				observe(map.getRobotHeading());

				

				amount++;
				if(amount >= 4){
					amount = 0;
					visitOverride = true;
				}
			}
		}
	}
	
	public void observe(int heading){
		ultrasonicSensor.clear();
		try {
			Thread.sleep(60*6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float F = ultrasonicSensor.getDistance();
		try {
			Thread.sleep(60*6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ultrasonicSensor.clear().rotate(90);
		try {
			Thread.sleep(60*6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float L = ultrasonicSensor.getDistance();
		ultrasonicSensor.clear().rotate(-180);
		try {
			Thread.sleep(60*6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		float R = ultrasonicSensor.getDistance();
		
		/*screen.writeTo(new String[]{
				"F: " + F,
				"L: " + L,
				"R: " + R,
				"H: " + heading
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());*/
		
		map.updateMap(heading, F, L, R);
		ultrasonicSensor.resetMotor();	
	}

	public boolean MoveSquares(int i){
		int direction = (i/Math.abs(i));
		
		
		for (int j = 0; j < i; j++) {
			if(direction == 1)
				pilot.forward();
			else
				pilot.backward();
			
			
			
			ColorNames cn;
			do{
				cn = colorSensor.getCurrentColor();
				if(bothBumpersPressed()){
					pilot.travel(-4.0f);
					return false;
				}
			}while(cn != ColorNames.BLACK);
			pilot.travel(11.0f);
			localisation.decreasePosiConfidence();
		}
		return true;
	}
	
	public void turnToHeading(int desiredHeading) {
		int initialHeading = map.getRobotHeading();
		int headingDifference = desiredHeading - initialHeading;
		int rotationAmount = 0;
		switch(headingDifference) {
			case 1: 
			case -3:
				rotationAmount = 90;
				localisation.decreaseOriConfidence();
				break;
			case 2: 
			case -2:
				rotationAmount = 180;
				localisation.decreaseOriConfidence();
				localisation.decreaseOriConfidence();
				break;
			case -1:
			case 3:
				rotationAmount = -90;
				localisation.decreaseOriConfidence();
				break;
		}
		pilot.rotate(rotationAmount);
		
		if(desiredHeading > 3)
			desiredHeading = 0;
		if(desiredHeading < 0)
			desiredHeading = 3;
		
		map.setRobotPos(map.getRobotX(), map.getRobotY(), desiredHeading);
	}
	
	public boolean bothBumpersPressed(){
		SampleProvider leftTouch = Robot.current.getLeftTouch();
		SampleProvider rightTouch = Robot.current.getRightTouch();
		

		float[] leftSample = Robot.current.getLeftSample();
		float[] rightSample = Robot.current.getRightSample();
		
		leftTouch.fetchSample(leftSample, 0);
    	rightTouch.fetchSample(rightSample, 0);
		
		return ((leftSample[0] > 0.9 && rightSample[0] > 0.9));
	}
	
	public boolean passedLine() {
		ColorNames cn = Robot.current.getColorSensor().getCurrentColor();
		if (cn == ColorNames.BLACK) {
			return true;
		} else {
			return false;
		}
	}

	public LCDRenderer getScreen(){
		return screen;
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
	
	public ColorSensorMonitor getColorSensor() {
		return colorSensor;
	}
	
	public Localisation getLocalisation() {
		return localisation;
	}
	
	public LED getLED() {
		return led;
	}
	
	public void setMapFinished(boolean mapFinished) {
		this.mapFinished = mapFinished;
	}
	
	public boolean getMapFinished() {
		return mapFinished;
	}
	
	public SoundMonitor getSoundMonitor() {
		return audio;
	}
}
