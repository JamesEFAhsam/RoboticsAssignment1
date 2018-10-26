package behaviours;

import lejos.hardware.Button;
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
	
	int squares;
	int amount;
	boolean visitOverride; 
	
	public IceSlideBehavior () {
		colorSensor = Robot.current.getColorSensor();
		pilot = Robot.current.getPilot();
		map = Robot.current.getMap();
		screen = Robot.current.getScreen();
		
		squares = 0;
		amount = 0;
		
		pilot.setLinearSpeed(10);
		
		map.setRobotPos(3, 4, 3);
		map.getTile(3, 5).view(false);
		
		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		suppressed = false;
		
		while(!suppressed && !Button.ESCAPE.isDown() && squares < 6 ) {
			screen.clearScreen();
			
			/*
			Robot.current.screen.writeTo(new String[]{
					"Ice Slide"
			}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
			Button.waitForAnyPress();
			*/
			
			screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
			
			
			if((!map.beenVisited(map.getRobotHeading()) || visitOverride) && map.canMove(map.getRobotHeading())){
				
				Robot.current.observe(map.getRobotHeading());
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				/*screen.writeTo(new String[]{
						"V: " + visitOverride
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());*/
				
				

				Robot.current.MoveSquares(1);
				
				map.moveRobotPos(map.getRobotHeading());
				
				Robot.current.observe(map.getRobotHeading());
				
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
				
				Robot.current.turnToHeading(map.getRobotHeading()+1);

				Robot.current.observe(map.getRobotHeading());

				

				amount++;
				if(amount >= 4){
					amount = 0;
					visitOverride = true;
				}
			}
			
			
			

			//screen.clearScreen();
			//screen.writeTo(new String[]{"F? "+(colorSensor.getColorFrequency() != null)});



			/*if(colorSensor.getCurrentColor() == ColorNames.BLACK && prevColor != ColorNames.BLACK){
				squares++;
			}

			if(colorSensor.getCurrentColor() != prevColor){
				prevColor = colorSensor.getCurrentColor();
			}

			screen.clearScreen();
			screen.writeTo(new String[]{"Passed through "+squares+" squares.",
					"Color. " + colorSensor.getCurrentColor(),
					"Previous. " + prevColor
					}, screen.getWidth()/2, 0, GraphicsLCD.HCENTER, Font.getSmallFont());
			screen.drawEscapeButton("QUIT", 0, 100, 45, 45/2, 6);
			*/

			
			//Button.waitForAnyPress();
			
			Thread.yield();
		}
		
		
		
	}

	public boolean takeControl() {
		return true;
	}
}
