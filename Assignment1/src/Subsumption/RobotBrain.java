package Subsumption;

import lejos.hardware.Brick;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;

public class RobotBrain {
	
	private Brick ev3;
	private EV3TouchSensor leftBump, rightBump;
	private EV3UltrasonicSensor uSensor;
	private EV3ColorSensor cSensor;
	private SampleProvider leftBumpSampleP, rightBumpSampleP, colourSampleP, ultrasonicDistSampleP;
	private float[] leftBumpSample, rightBumpSample, colourSample, ultrasonicDistSample;
	private Arbitrator arbitrator;
	private MovePilot pilot;
	
	public OdometryPoseProvider poseProvider;
	
	//Current location of the robot (cell reference)
	private int robotGlobalPoseX = 0;
	private int robotGlobalPoseY = 0;
	
	// CONSTANTS
	private static final double ARENAHEIGHT = 7;
	private static final double ARENAWIDTH = 6;
	
	private static final double CELLSIZE = 25; //in cm 
	
	
	// Specific to robot
	private static final double DIAMETER = 3.3;
	private static final double OFFSET = 10;
	private static final double ANGULAR_SPEED = 50;
	private static final double ANGULAR_ACCELERATION = 200;
	
	
	
	public static void main(String[] args) {
		RobotBrain robot = new RobotBrain();
		
	}
}
