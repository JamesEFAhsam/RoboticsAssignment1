package net.robotics.main;

import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import net.robotics.map.Map;
import net.robotics.screen.LCDRenderer;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;

public class IceSlide {	
	public static void iceSlide(Robot robot){
		int squares = 0;
		
		ColorNames prevColor = robot.getColorSensor().getColor();
		MovePilot pilot = robot.getPilot();
		Map map = robot.getMap();
		LCDRenderer screen = robot.getScreen();
		
		
		int amount = 0;
		boolean visitOverride = false; 

		pilot.setLinearSpeed(10);
		
		/*map.setRobotPos(2, 0, 0);
		//map.updateMap(3, 0.1f, 0.1f, 0.1f);
		//map.updateTiles(2, 0.1f);

		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);*/
		
		//Button.waitForAnyPress();
		
		map.setRobotPos(3, 4, 3);
		map.getTile(3, 5).view(false);
		
		screen.clearScreen();
		screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);


		Button.waitForAnyPress();

		 while(!Button.ESCAPE.isDown() && squares < 6 ){
			screen.clearScreen();
			screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
			
			if((!map.beenVisited(map.getRobotHeading()) || visitOverride) && map.canMove(map.getRobotHeading())){
				
				robot.observe(map.getRobotHeading());
				
				screen.drawMap(screen.getWidth()-8-map.getWidth()*16, -4, map);
				/*screen.writeTo(new String[]{
						"V: " + visitOverride
				}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());*/
				
				robot.MoveSquares(1);
				
				map.moveRobotPos(map.getRobotHeading());
				
				robot.observe(map.getRobotHeading());
				
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
				
				robot.turnToHeading(map.getRobotHeading()+1);

				robot.observe(map.getRobotHeading());
				
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
		}
	}
}


