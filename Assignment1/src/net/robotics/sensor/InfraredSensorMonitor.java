package net.robotics.sensor;

import java.util.ArrayList;
import java.util.Arrays;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.EV3IRSensor;
import net.robotics.main.Robot;

public class InfraredSensorMonitor extends Thread{
	
	private EV3IRSensor irSensor;
	private NXTRegulatedMotor motor;
	private float[] sample;
	
	public float distance[];
	public int pointer;
	
	private int amount;
	
	private Robot robot;
	private int Delay;
	
	public InfraredSensorMonitor(Robot robot, EV3IRSensor sensor, NXTRegulatedMotor motor, int Delay){
		this.setDaemon(true);
		this.irSensor = sensor;
		this.motor = motor;
		
		this.motor.setAcceleration(5000);
		this.motor.setSpeed(720);
		
		this.sample = new float[sensor.getDistanceMode().sampleSize()];
		this.robot = robot;
		this.Delay = Delay;
		
		this.distance = new float[5];
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
		irSensor.getDistanceMode().fetchSample(sample, 0);
		return sample[0];
	}
	
	public float getMedianDistance(){
		float[] values = distance.clone();
		Arrays.sort(values);
		return values[values.length/2];
	}
	
	public InfraredSensorMonitor rotate(int degrees){
		motor.rotate(degrees);
		return this;
	}
	
	public InfraredSensorMonitor resetMotor(){
		motor.rotateTo(0);
		return this;
	}
	
	public InfraredSensorMonitor clear(){
		for (int i = 0; i < distance.length; i++) {
			distance[i] = 0;
		}
		amount = 0;
		return this;
	}
	
	public void run() {
		
		
		while(true){
			
			float dist = getDistance();
			if(dist > 1000f)
				continue;
			
			distance[pointer] = dist;
			
			pointer++;
			if(pointer>=5)
				pointer = 0;
			
			amount++;
			
			
			
			try{
				sleep(Delay);
			} catch(Exception e){
				
			}
		}
	}
}
