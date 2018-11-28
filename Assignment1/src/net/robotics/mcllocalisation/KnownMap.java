package net.robotics.mcllocalisation;

import net.robotics.map.Tile;

public class KnownMap {
	private Tile[][] tiles;
	private int width, height;	
	
	public KnownMap(int width, int height, int[][]oc) {
		this.setWidth(width);
		this.setHeight(height);
		this.setTiles(width, height);
		placeObstacles(oc);
	}
	
	private void setTiles(int width, int height) {
		this.tiles = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.tiles[x][y] = new Tile(x, y);
			}
		}
	}
	
	public Tile getTile(int x, int y){
		if(x >= 0 && x < width && y >= 0 && y < height)
			return tiles[x][y];
		return null;
	}
	
	public void placeObstacles(int[][] oc) {
		for (int i = 0; i < oc.length; i++) {
			int x = oc[i][0];
			int y = oc[i][1];
			tiles[x][y].knownObstacle();
		}
	}
	
	public int getWidth() {
		return width;
	}

	private void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}

	private void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isPointIn(int x, int y) {
		if(x >= 0 && x < width && y >= 0 && y < height) {
			return true;
		} else {
			return false;
		}
	}

	public boolean notObstacle(int x, int y) {
		if (getTile(x,y).getOccupiedBelief() == 1.0) {
			return false;
		} else {
			return true;
		}
	}
}
