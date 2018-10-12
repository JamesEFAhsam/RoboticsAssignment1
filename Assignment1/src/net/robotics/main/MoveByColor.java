package net.robotics.main;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.internal.ev3.EV3LED;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;

public class MoveByColor {

	private static RGBFloat GREEN = new RGBFloat(new RGBFloat(0f, 0.19f, 0f), new RGBFloat(0.15f, 0.25f, 0.15f));
	private static RGBFloat WHITE = new RGBFloat(new RGBFloat(0.22f, 0.22f, 0.22f), new RGBFloat(0.3f, 0.3f, 0.3f));
	private static RGBFloat BLACK = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.00f), new RGBFloat(0.05f, 0.05f, 0.05f));
	private static RGBFloat BLUE = new RGBFloat(new RGBFloat(0.00f, 0.00f, 0.05f), new RGBFloat(0.075f, 0.055f, 0.1f));
	private static RGBFloat YELLOW = new RGBFloat(new RGBFloat(0.25f, 0.15f, 0.00f), new RGBFloat(0.30f, 0.20f, 0.1f));
	private static RGBFloat RED = new RGBFloat(new RGBFloat(0.20f, 0.00f, 0.00f), new RGBFloat(0.25f, 0.05f, 0.075f));

	
	private GraphicsLCD lcd;
	private EV3LED led;
	private MovePilot pilot;
	private OdometryPoseProvider opp;

	public static void main(String[] args){
		new MoveByColor();
	}

	public MoveByColor() {

		// Set up the wheels by specifying the diameter of the
		// left (and right) wheels in centimeters, i.e. 4.05 cm.
		// The offset number is the distance between the centre
		// of wheel to the centre of robot (4.9 cm)
		// NOTE: this may require some trial and error to get right!!!
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 4.05).offset(-4.9);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 4.05).offset(4.9);

		Chassis myChassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(myChassis);

		// Create a pose provider and link it to the move pilot
		opp = new OdometryPoseProvider(pilot);

		Brick myEV3 = BrickFinder.getDefault();

		//leftBump = new EV3TouchSensor(myEV3.getPort("S1"));
		//rightBump = new EV3TouchSensor(myEV3.getPort("S4"));

		//leftSP = leftBump.getTouchMode();
		//rightSP = rightBump.getTouchMode();

		//leftSample = new float[leftSP.sampleSize()];		// Size is 1
		//rightSample = new float[rightSP.sampleSize()];		// Size is 1
		
		colorSensor = new EV3ColorSensor(myEV3.getPort("S2"));
		colorSample = new float[colorSensor.getRGBMode().sampleSize()];

		pilot = new MovePilot(myChassis);
		
		ColorMonitor myMonitor = new ColorMonitor(this, 100);
		myMonitor.start();
		
		while(!Button.ESCAPE.isDown()){
			pilot.travel(25);
			
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

			//led.setPattern(EV3LED.COLOR_NONE, EV3LED.PATTERN_ON);
			return "BLACK";
		}
		
		if(color.Compare(BLUE)){
			return "BLUE";
		}
		
		if(color.Compare(RED)){
			//led.setPattern(EV3LED.COLOR_RED, EV3LED.PATTERN_ON);
			return "RED";
		}
		
		if(color.Compare(YELLOW)){
			//led.setPattern(EV3LED.COLOR_ORANGE, EV3LED.PATTERN_ON);

			return "YELLOW";
		}
		
		
		
		if(color.Compare(GREEN)){
			//led.setPattern(EV3LED.COLOR_GREEN, EV3LED.PATTERN_ON);
			return "GREEN";
		}
		
		if(color.Compare(WHITE)){
			//led.setPattern(EV3LED.COLOR_ORANGE, EV3LED.PATTERN_BLINK);

			return "WHITE";
		}
		
		return "UNKNOWN";
	}
	
	public void closeRobot() {
		//leftBump.close();
		//rightBump.close();
		colorSensor.close();
	}

	/*public boolean isLeftBumpPressed() {
		leftSP.fetchSample(leftSample, 0);
		return (leftSample[0] == 1.0);
	}

	public boolean isRightBumpPressed() {
		rightSP.fetchSample(rightSample, 0);
		return (rightSample[0] == 1.0);
	}

	public float getDistance() {
		distSP.fetchSample(distSample, 0);
		return distSample[0];
	}

	public float getAngle() {
		gyroSP.fetchSample(angleSample, 0);
		return angleSample[0];
	}*/

	public MovePilot getPilot() {
		return pilot;
	}

}
