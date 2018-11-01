package net.robotics.communication;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.robotics.map.Map;
import net.robotics.map.Tile;

public class MapCanvas extends JPanel {
	private static final long serialVersionUID = 1L;

	private int tileSize = 64;
	private final String name = "FENTON!";
	private Map map;
	//private Map loadedMap;


	public MapCanvas() {

		this.setPreferredSize(new Dimension(700,600));
	}

	public void UpdateMap(Map map){
		this.map = map;
		paintComponent(this.getGraphics());
	}

	private void drawMap(Graphics g){

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		int xShift = 100;
		int yShift = 0;

		for (int dx = 0; dx < map.getWidth(); dx++) {
			for (int dy = 0; dy < map.getHeight(); dy++) {
				int x = dx;
				int y = (map.getHeight() - dy)-1;
				Tile currentTile = map.getTile(dx, dy);
				
				g.setColor(Color.BLUE);
				g.drawRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);

				if (dx == 0 && dy == 0) {
					g.setColor(Color.BLUE);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (dx == map.getWidth()-1 && dy == 0) {
					g.setColor(Color.GREEN);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (dx == 0 && dy == map.getHeight()-1) {
					g.setColor(Color.YELLOW);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (dx == map.getWidth()-1 && dy == map.getHeight()-1) {
					g.setColor(Color.RED);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}

				
				String secline = "V: " + currentTile.getViewed();
				String frsline = "F:" + (currentTile.getViewed() - currentTile.getEmpty());
				String thrline = "Visits:" + currentTile.getVisitAmount();
				String posline = "("+currentTile.getX()+","+currentTile.getY()+")";
				String fifline = "OB:" +(int)(currentTile.getOccupiedBelief()*100f) + "%";



				if (currentTile.getOccupiedBelief() > Map._OCCUPIEDBELIEFCUTOFF) {
					g.setColor(Color.BLACK);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);	

					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.BLACK);
				}
				

				g.drawString(posline, (x*tileSize)+xShift+2, (y*tileSize)+yShift+12);
				if (dx == map.getRobotX() && dy == map.getRobotY()) {
					g.drawString(name, (x*tileSize)+xShift+2, (y*tileSize)+yShift+40);
				} else {
					g.drawString(frsline, (x*tileSize)+xShift+2, (y*tileSize)+yShift+24);
					g.drawString(secline, (x*tileSize)+xShift+2, (y*tileSize)+yShift+36);
					g.drawString(thrline, (x*tileSize)+xShift+2, (y*tileSize)+yShift+48);
					g.drawString(fifline, (x*tileSize)+xShift+2, (y*tileSize)+yShift+60);
				}
			}
		}	
		
		g.setColor(Color.BLUE);
		g.drawRect((map.getWidth()*tileSize)+xShift+40, yShift, 200, 100);
		g.drawString("Key:", (map.getWidth()*tileSize)+xShift+40+2, yShift+12);
		g.setColor(Color.BLACK);
		g.drawString("V: Viewed", (map.getWidth()*tileSize)+xShift+40+2, yShift+24);
		g.drawString("F: Times Viewed Occupied", (map.getWidth()*tileSize)+xShift+40+2, yShift+36);
		g.drawString("Visits: Times Visited", (map.getWidth()*tileSize)+xShift+40+2, yShift+48);
		g.drawString("OB: Occupied Belief", (map.getWidth()*tileSize)+xShift+40+2, yShift+60);

		Tile currentTile = map.getTile(map.getRobotX(), map.getRobotY());
		
		String secline = "V: " + currentTile.getViewed();
		String frsline = "F:" + (currentTile.getViewed() - currentTile.getEmpty());
		String thrline = "Visits:" + currentTile.getVisitAmount();
		String posline = "("+currentTile.getX()+","+currentTile.getY()+")";
		String fifline = "OB:" +(int)(currentTile.getOccupiedBelief()*100f) + "%";
		
		g.setColor(Color.BLUE);
		g.drawRect((map.getWidth()*tileSize)+xShift+40, yShift+110, 100, 100);
		g.drawString("Fenton's Info:", (map.getWidth()*tileSize)+xShift+40+2, yShift+110+12);
		g.setColor(Color.BLACK);
		g.drawString(secline, (map.getWidth()*tileSize)+xShift+40+2, yShift+110+24);
		g.drawString(frsline, (map.getWidth()*tileSize)+xShift+40+2, yShift+110+36);
		g.drawString(thrline, (map.getWidth()*tileSize)+xShift+40+2, yShift+110+48);
		g.drawString(posline, (map.getWidth()*tileSize)+xShift+40+2, yShift+110+60);
		g.drawString(fifline, (map.getWidth()*tileSize)+xShift+40+2, yShift+110+72);
	}




	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(map == null)
			return;

		drawMap(g);

	}
}