package net.robotics.sensor;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import net.robotics.main.Robot;

public class UltrasonicSensorMonitor extends Thread{
	
	private EV3UltrasonicSensor ultrasonicSensor;
	private NXTRegulatedMotor motor;
	private float[] sample;
	
	private Robot robot;
	private int Delay;
	
	public UltrasonicSensorMonitor(Robot robot, EV3UltrasonicSensor sensor, NXTRegulatedMotor motor, int Delay){
		this.setDaemon(true);
		this.ultrasonicSensor = sensor;
		this.motor = motor;
		
		this.motor.setAcceleration(3000);
		
		this.sample = new float[sensor.getDistanceMode().sampleSize()];
		this.robot = robot;
		this.Delay = Delay;
	}
	
	public synchronized void start() {
		super.start();
	}



	public void configure(){
	}
	
	public boolean isObjectDirectInFront(){
		float distance = getDistance();
		return distance <= 0.15f && distance >= 0.005f;
	}
	
	public float getDistance(){
		ultrasonicSensor.getDistanceMode().fetchSample(sample, 0);
		return sample[0];
	}
	
	public UltrasonicSensorMonitor rotate(int degrees){
		motor.rotate(degrees);
		return this;
	}
	
	public UltrasonicSensorMonitor resetMotor(){
		motor.rotateTo(0);
		return this;
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
