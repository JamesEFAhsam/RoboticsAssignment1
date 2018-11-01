

package net.robotics.behaviours;

import java.io.File;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.map.Tile;


public class Finalisation implements Behavior{
	public boolean suppressed;
	public final int _N_TIMES_VIEWED = 10;
	
	private Map map;
	private int width;
	private int height;
	
	
	public Finalisation() {
		map = Robot.current.getMap();
		width = map.getWidth();
		height = map.getHeight();
	}
	

	public void suppress() {
		suppressed = true;
	}

	public void action() {		
		File f = new File("tada.wav");
		Robot.current.getSoundMonitor().playSound(f);
		suppressed = false;

		while(!suppressed) {
			displayScreen();
			// Display "finished mapping, press escape to exit", to the side of the map
		}
		
		Robot.current.closeProgram();
	}
	
	public void displayScreen(){
		Robot.current.screen.clearScreen();
		Robot.current.screen.writeTo(new String[]{
				"Fin."
		}, Robot.current.screen.getWidth(), 0, GraphicsLCD.RIGHT, Font.getSmallFont());
		Robot.current.screen.drawMap(Robot.current.screen.getWidth()-8-map.getWidth()*16, -4, map);
	}

	public boolean takeControl() {
		return(isMapFinished());
	}
	
	public boolean isMapFinished() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map.getTile(x, y).getViewed() < _N_TIMES_VIEWED && map.getTile(x,y).isReachable()) {
					return false;
				}
			}
		}
		return true;
	}
}
