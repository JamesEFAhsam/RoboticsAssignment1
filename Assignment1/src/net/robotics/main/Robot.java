package net.robotics.main;


import java.io.IOException;
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
import net.robotics.communication.ServerSide;

public class Robot {
	public LCDRenderer screen;
	private ColorSensorMonitor leftColorSensor, rightColorSensor;
	private LED led;
	private SoundMonitor audio;
	private MovePilot pilot;
	private OdometryPoseProvider opp;
	private Map map;
	private Localisation localisation;
	private CustomArbitrator arbitrator;
	private ServerSide server;
	
	public int overrideVisitAmount = 0;
	
	private boolean mapFinished;
	
	private LocaliseBehavior b1;
	private IceSlideBehavior b2;
	private AStar b3;
	private Finalisation b4;
	
	public static Robot current;
	


	public static void main(String[] args) throws IOException{
		new Robot();
		current.startRobot(false);
	}

	public Robot() {
		current = this;
		
		Brick myEV3 = BrickFinder.getDefault();
		led = myEV3.getLED();
		audio = new SoundMonitor(myEV3.getAudio());
		screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());
		//server = new ServerSide();
		screen.clearScreen();
		screen.writeTo(new String[]{
				"Booting..."
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		leftColorSensor = new ColorSensorMonitor(this, new EV3ColorSensor(myEV3.getPort("S1")), 16);
		rightColorSensor = new ColorSensorMonitor(this, new EV3ColorSensor(myEV3.getPort("S4")), 16);
		localisation = new Localisation();
		
		NXTRegulatedMotor motor = null;
		EV3UltrasonicSensor ultra = null;
		
		while(true){
			try {
				//ultra = new EV3UltrasonicSensor(myEV3.getPort("S3"));
				motor = Motor.C;
				break;
			} catch(Exception e) {
			}
		}
		
		/*ultrasonicSensor = new UltrasonicSensorMonitor(this, 
				ultra, 
				motor, 60);*/

		//screen.writeTo(new String[]{"Alive? "+(colorSensor != null)});

		this.map = new Map(6, 6);

		leftColorSensor.configure(false); //Only need to configure once as it sets a static object
		leftColorSensor.start();
		
		rightColorSensor.start();
		
		audio.start();
	}
	
	private void startRobot(boolean useArbitrator) {
		pilot = ChasConfig.getPilot();
		pilot.setLinearSpeed(11);
		
		// Create a pose provider and link it to the move pilot
		opp = new OdometryPoseProvider(pilot);
		
		server = new ServerSide(100);
		server.start();
		
		setUpBehaviors();
		
		createKeyListeners();
		
		if(useArbitrator){
			arbitrator.go();
		} else {
			mainLoop();
		}
			
	}
	
	private void setUpBehaviors() {
		b1 = new LocaliseBehavior();
		b2 = new IceSlideBehavior();
		b3 = new AStar(getMap());
		b4 = new Finalisation();
		Behavior[] behaviors = {b3, b2, b1, b4};			// Behavior priority, where [0] is lowest priority
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
			}
			
			public void keyPressed(Key k) {
			}
		});
		
		
		Button.RIGHT.addKeyListener(new KeyListener() {
			public void keyReleased(Key k) {
				Robot.current.getScreen().cycleMode();
			}
			
			public void keyPressed(Key k) {
			}
		});
		
		Button.DOWN.addKeyListener(new KeyListener() {
			public void keyReleased(Key k) {
				Robot.current.resetScreen();
			}
			
			public void keyPressed(Key k) {
			}
		});
	}

	public void mainLoop(){
		pilot.rotate((Math.random()*25)-45);
		pilot.forward();
		
		boolean foundblack = false;
		ColorSensorMonitor onBlack, other;
		
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			screen.clearScreen();
			screen.writeTo(new String[]{
					"L: " + leftColorSensor.getColor(),
					"R: " + rightColorSensor.getColor(),
					"L: " + leftColorSensor.getBlueColor(),
					"R: " + rightColorSensor.getBlueColor()
			}, 0, 0, GraphicsLCD.LEFT, Font.getDefaultFont());
			
			if(leftColorSensor.getColor() == ColorNames.BLACK || rightColorSensor.getColor() == ColorNames.BLACK){
				if(leftColorSensor.getColor() == ColorNames.BLACK && rightColorSensor.getColor() != ColorNames.BLACK){
					
					if(!foundblack){
						foundblack = true;
						pilot.stop();
					}
					
					screen.clearScreen();
					screen.writeTo(new String[]{
							"L: " + leftColorSensor.getColor(),
							"R: " + rightColorSensor.getColor(),
							"L: OnBlack: " + leftColorSensor.getBlueColor(),
							"R: other: " + rightColorSensor.getBlueColor()
					}, 0, 0, GraphicsLCD.LEFT, Font.getDefaultFont());
					
					onBlack = leftColorSensor;
					other = rightColorSensor;
					
					pilot.rotate(-5);
					pilot.travel(0.5f);
				} else if(leftColorSensor.getColor() != ColorNames.BLACK && rightColorSensor.getColor() == ColorNames.BLACK){
					if(!foundblack){
						foundblack = true;
						pilot.stop();
					}
					
					screen.clearScreen();
					screen.writeTo(new String[]{
							"L: " + leftColorSensor.getColor(),
							"R: " + rightColorSensor.getColor(),
							"L: other: " + leftColorSensor.getBlueColor(),
							"R: OnBlack: " + rightColorSensor.getBlueColor()
					}, 0, 0, GraphicsLCD.LEFT, Font.getDefaultFont());
					
					other = leftColorSensor;
					onBlack = rightColorSensor;
					
					pilot.rotate(5);
					pilot.travel(0.5f);
				} else if(leftColorSensor.getColor() == ColorNames.BLACK && rightColorSensor.getColor() == ColorNames.BLACK){
					pilot.stop();
					
					screen.clearScreen();
					screen.writeTo(new String[]{
							"L: " + leftColorSensor.getColor(),
							"R: " + rightColorSensor.getColor(),
							"L:" + leftColorSensor.getBlueColor(),
							"R:" + rightColorSensor.getBlueColor(),
							"Exited."
					}, 0, 0, GraphicsLCD.LEFT, Font.getDefaultFont());
					
					return; //return if localised
				}
			}
		}
		
		
	}
	
	public void observe(int heading){
	}

	public boolean MoveSquares(int i){
		
		//TODO Create Move Squares
		return false;
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
	
	@Deprecated
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
		ColorNames lcn = Robot.current.getLeftColorSensor().getCurrentColor();
		ColorNames rcn = Robot.current.getRightColorSensor().getCurrentColor();
		if (lcn == ColorNames.BLACK || rcn == ColorNames.BLACK) {
			return true;
		} else {
			return false;
		}
	}
	
	public void resetScreen(){
		
		LocalEV3.get().getTextLCD().clear();
		LocalEV3.get().getGraphicsLCD().clear();
			
		screen.clearScreen();
		screen = new LCDRenderer(LocalEV3.get().getGraphicsLCD());
		screen.clearScreen();
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
	
	@Deprecated
	public float[] getLeftSample() {
		return null;
	}
	
	@Deprecated
	public float[] getRightSample() {
		return null;
	}
	
	@Deprecated
	public SampleProvider getLeftTouch() {
		return null;
	}
	
	@Deprecated
	public SampleProvider getRightTouch() {
		return null;
	}
	
	public ColorSensorMonitor getLeftColorSensor() {
		return leftColorSensor;
	}
	
	public ColorSensorMonitor getRightColorSensor() {
		return rightColorSensor;
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
