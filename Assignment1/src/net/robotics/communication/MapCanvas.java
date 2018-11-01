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
		
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				g.setColor(Color.BLUE);
				g.drawRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);

				if (x == 0 && y == 0) {
					g.setColor(Color.YELLOW);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (x == map.getWidth()-1 && y == 0) {
					g.setColor(Color.RED);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (x == 0 && y == map.getHeight()-1) {
					g.setColor(Color.BLUE);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}else if (x == map.getWidth()-1 && y == map.getHeight()-1) {
					g.setColor(Color.GREEN);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);
				}

				Tile currentTile = map.getTile(x, y);
				String secline = "Visisted: " + currentTile.getVisitAmount();
				String frsline = "Empty:" + currentTile.getEmpty();
				String thrline = "Visited fully:" + (currentTile.getVisitAmount() - currentTile.getEmpty());
				String forline = "(x,y) =" + "("+currentTile.getX()+","+currentTile.getY()+")";
				String fifline = "O.B.:" +currentTile.getOccupiedBelief();

				if (x == map.getRobotX() && y == map.getRobotY()) {
					g.drawString(name, (x*tileSize)+xShift+20, (y*tileSize)+yShift+40);
				}

				if (currentTile.getOccupiedBelief() > Map._OCCUPIEDBELIEFCUTOFF) {
					g.setColor(Color.BLACK);
					g.fillRect((x*tileSize)+xShift, (y*tileSize)+yShift, tileSize, tileSize);	
					
					g.setColor(Color.WHITE);
					g.drawString(frsline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+10);
					g.drawString(secline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+25);
					g.drawString(thrline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+40);
					g.drawString(forline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+55);
					g.drawString(fifline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+70);
				} else {
					g.setColor(Color.BLACK);
					g.drawString(frsline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+10);
					g.drawString(secline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+25);
					g.drawString(thrline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+40);
					g.drawString(forline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+55);
					g.drawString(fifline, (x*tileSize)+xShift+5, (y*tileSize)+yShift+70);
				}

			}
		}	
	}

	


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(map == null)
			return;

		drawMap(g);
		
	}
}