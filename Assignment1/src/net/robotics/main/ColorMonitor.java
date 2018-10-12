package net.robotics.main;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

public class ColorMonitor extends Thread{
	private int delay;
	public MoveByColor robot;

	GraphicsLCD lcd = LocalEV3.get().getGraphicsLCD();

	// Make the monitor a daemon and set
	// the robot it monitors and the delay
	public ColorMonitor(MoveByColor r, int d){
		this.setDaemon(true);
		delay = d;
		robot = r;
	}

	@Override
	public void run() {
		while(true){

			lcd.clear();

			lcd.drawString("Reading: ", lcd.getWidth()/2, 0, GraphicsLCD.HCENTER);
			lcd.drawString("R("+robot.getRedColor()+")", lcd.getWidth()/2, 20, GraphicsLCD.HCENTER);
			lcd.drawString("G("+robot.getGreenColor()+")", lcd.getWidth()/2, 40, GraphicsLCD.HCENTER);
			lcd.drawString("B("+robot.getBlueColor()+")", lcd.getWidth()/2, 60, GraphicsLCD.HCENTER);
			lcd.drawString("Color("+robot.getColor()+")", lcd.getWidth()/2, 80, GraphicsLCD.HCENTER);

			try{
				sleep(delay);
			}
			catch(Exception e){
				// We have no exception handling
				;
			}
		}
	}


}
