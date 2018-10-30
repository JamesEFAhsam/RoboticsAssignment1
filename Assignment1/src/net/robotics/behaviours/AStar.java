package net.robotics.behaviours;
import java.util.LinkedList;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;
import net.robotics.map.AStarSearch;
import net.robotics.map.Map;
import net.robotics.map.Tile;

public class AStar implements Behavior {
	public boolean suppressed;

	private LinkedList<Tile> path;
	private AStarSearch search;
	private Map map;
	
	private boolean justRan = false;
	
	private Tile leastKnown;

	public AStar (Map map) {
		this.map = map;
		this.search = new AStarSearch(map);
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		Robot.current.getLED().setPattern(1);
		Robot.current.screen.clearScreen();
		Robot.current.screen.writeTo(new String[]{
				"Running: "
		}, 0, 20, GraphicsLCD.LEFT, Font.getDefaultFont());

		int iteration = 0;
		
		suppressed = false;

		while (!suppressed) {
			Robot.current.screen.clearScreen();
			Robot.current.screen.writeTo(new String[]{
					"A Star: " + iteration
			}, 0, 0, GraphicsLCD.LEFT, Font.getSmallFont());

			iteration++;

			Tile robotTile = map.getTile(map.getRobotX(), map.getRobotY());
			
			
			if(path == null || path.isEmpty()){
				
				leastKnown = search.getLeastVisitedNode();
				path = search.searchForPath(robotTile, leastKnown);
			}

			Tile nextTile = path.pop();

			int heading = map.getHeading(nextTile.getX() - robotTile.getX(), 
					nextTile.getY() - robotTile.getY());
			
			Robot.current.screen.drawMap(Robot.current.screen.getWidth()-8-map.getWidth()*16, -4, map);

			Robot.current.screen.writeTo(new String[]{
					"T: " + nextTile.getX() + "/" + nextTile.getY()
			}, 0, 20, GraphicsLCD.LEFT, Font.getSmallFont());
			
			Robot.current.screen.writeTo(new String[]{
					"N: " + leastKnown.getX() + "/" + leastKnown.getY()
			}, 0, 40, GraphicsLCD.LEFT, Font.getSmallFont());

			if(heading != map.getRobotHeading()){
				Robot.current.observe(map.getRobotHeading());
				Robot.current.turnToHeading(heading);
			}


			Robot.current.observe(map.getRobotHeading());
			boolean sucessful = Robot.current.MoveSquares(1);

			if(!sucessful){
				map.getTile(map.getRobotHeading()).view(false);
			} else {
				map.moveRobotPos(map.getRobotHeading());
			}

			if(Button.ESCAPE.isDown())
				Robot.current.closeProgram();
			
			if(map.getRobotX() == leastKnown.getX() && map.getRobotY() == leastKnown.getY()){
				Robot.current.overrideVisitAmount = 0;
				justRan = true;
				suppress();
			}
			
			Thread.yield();
		}
	}

	public boolean takeControl() {
		if(justRan){
			justRan = false;
			return false;
		}
		return true;
	}
}
