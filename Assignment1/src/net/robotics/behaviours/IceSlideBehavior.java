package net.robotics.behaviours;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.screen.LCDRenderer;
import net.robotics.sensor.ColorSensorMonitor;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;

public class IceSlideBehavior implements Behavior{
	public boolean suppressed;
	private ColorSensorMonitor colorSensor;
	private MovePilot pilot;
	private Map map;
	
	private int iteration = 0;
	private boolean visitOverride = false;

	public IceSlideBehavior () {
		//colorSensor = Robot.current.getColorSensor();
		pilot = Robot.current.getPilot();
		map = Robot.current.getMap();
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		File f = new File("panting.wav");
		Robot.current.getSoundMonitor().playSound(f);
		Robot.current.getLED().setPattern(3);
		suppressed = false;

		

		while(!suppressed){

			if(Button.ESCAPE.isDown())
				Robot.current.closeProgram();
			
			Robot.current.observe(map.getRobotHeading());
			displayScreen();

			if((!map.beenVisited(map.getRobotHeading()) || visitOverride) && map.canMove(map.getRobotHeading())){
				//then move

				boolean sucessful = Robot.current.MoveSquares(1);
				
				if(!sucessful){
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
					map.getTile(map.getRobotHeading()).view(false);
				}else {
					map.moveRobotPos(map.getRobotHeading());
	
					visitOverride = false;
				}
				
				displayScreen();
			} else{
				
				boolean isFreeSquare = false;
				
				for (int i = 0; i < 3; i++) {
					if(!map.beenVisited(i) && map.canMove(i)){
						Robot.current.turnToHeading(i);
						displayScreen();
						isFreeSquare = true;
					}
				}
				
				if(!isFreeSquare){
					visitOverride = true;
					Robot.current.overrideVisitAmount++;
				}
			}
			
			if(Robot.current.overrideVisitAmount > 0){
				suppress();
			}
			
			

			iteration++;
			Thread.yield();
		}

	}
	
	public void displayScreen(){
		Robot.current.screen.clearScreen();
		Robot.current.screen.writeTo(new String[]{
				"Ice Slide: " + iteration
		}, Robot.current.screen.getWidth(), 0, GraphicsLCD.RIGHT, Font.getSmallFont());
		Robot.current.screen.drawMap(Robot.current.screen.getWidth()-8-map.getWidth()*16, -4, map);
	}

	public boolean takeControl() {
		if(Robot.current.overrideVisitAmount > 0){
			visitOverride = false;
			return false;
		}
		return true;
	}
}
