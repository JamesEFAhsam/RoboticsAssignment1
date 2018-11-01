

package net.robotics.behaviours;

import java.io.File;
import java.util.ArrayList;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.map.AStarSearch;


public class Finalisation implements Behavior{
	public boolean suppressed;
	public final int _N_TIMES_VIEWED = 6;

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
			
			try {
				Thread.sleep((long) (250));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		ArrayList<Tile> openSet = new ArrayList<Tile>();
		ArrayList<Tile> closedSet = new ArrayList<Tile>();

		openSet.add(map.getTile(map.getRobotX(), map.getRobotY()));

		while(!openSet.isEmpty()){
			Tile chosen = openSet.get(0);

			if(chosen.getViewed() < _N_TIMES_VIEWED && chosen.getVisitAmount() < 1){
				return false;
			}

			if(map.canMove(chosen.getX(), chosen.getY())){
				for (int x = -1; x < 2; x++) {
					for (int y = -1; y < 2; y++) {
						if((x != 0 && y != 0) || (x == y && x == 0))
							continue;

						int nX = (chosen.getX()+x);
						int nY = (chosen.getY()+y);

						if(nX<0 || nX>=map.getWidth() || nY<0 || nY>=map.getHeight()){
							continue;
						}
						

						Tile neighbor = map.getTile(nX, nY);
						
						if(neighbor == null)
							continue;
						
						if(AStarSearch.isInSet(openSet, neighbor)){
							continue;
						} else if(AStarSearch.isInSet(closedSet, neighbor)){
							continue;
						}
						
						openSet.add(neighbor);
					}
				}
			}

			openSet.remove(chosen);
			closedSet.add(chosen);
		}
		return true;
	}
}
