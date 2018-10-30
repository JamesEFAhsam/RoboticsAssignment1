package net.robotics.behaviours;

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
	private LCDRenderer screen;
	
	private int iteration = 0;
	private int amount = 0;
	private boolean visitOverride = false;

	public IceSlideBehavior () {
		colorSensor = Robot.current.getColorSensor();
		pilot = Robot.current.getPilot();
		map = Robot.current.getMap();
		screen = Robot.current.getScreen();
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;

		

		while(!suppressed){
			Robot.current.screen.clearScreen();
			Robot.current.screen.writeTo(new String[]{
					"Ice Slide: " + iteration,
					"Amount: " + Robot.current.overrideVisitAmount
			}, 0, 0, GraphicsLCD.LEFT, Font.getSmallFont());

			

			if(Button.ESCAPE.isDown())
				Robot.current.closeProgram();
			
			Robot.current.observe(map.getRobotHeading());
			screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);

			if((!map.beenVisited(map.getRobotHeading()) || visitOverride) && map.canMove(map.getRobotHeading())){
				//then move

				boolean sucessful = Robot.current.MoveSquares(1);
				
				if(!sucessful)
					map.getTile(map.getRobotHeading()).view(false);
				else {
					map.moveRobotPos(map.getRobotHeading());
	
					Robot.current.observe(map.getRobotHeading());
					screen.clearScreen();
					Robot.current.screen.writeTo(new String[]{
							"Ice Slide: " + iteration,
							"Amount: " + Robot.current.overrideVisitAmount
					}, 0, 0, GraphicsLCD.LEFT, Font.getSmallFont());
					screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
	
					visitOverride = false;
					amount = 0;
				}

			} else{
				//then rotate to find 

				Robot.current.turnToHeading(map.getRobotHeading()+1);
				Robot.current.observe(map.getRobotHeading());
				screen.clearScreen();
				Robot.current.screen.writeTo(new String[]{
						"Ice Slide: " + iteration,
						"Amount: " + Robot.current.overrideVisitAmount
				}, 0, 0, GraphicsLCD.LEFT, Font.getSmallFont());
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);

				amount++;
				if(amount >= 4){
					amount = 0;
					visitOverride = true;
					Robot.current.overrideVisitAmount++;
				}
			}
			
			if(Robot.current.overrideVisitAmount > 0){
				suppress();
			}
			
			if(Button.ESCAPE.isDown())
				Robot.current.closeProgram();

			iteration++;
			Thread.yield();
		}

	}

	public boolean takeControl() {
		if(Robot.current.overrideVisitAmount > 0){
			visitOverride = false;
			return false;
		}
		return true;
	}
}
