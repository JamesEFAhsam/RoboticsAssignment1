package net.robotics.map;
//import net.robotics.map.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import net.robotics.main.Robot;

public class AStarSearch {
	
	protected ArrayList<Tile> openSet, closedSet, cameFrom, path;
	public PriorityQueue<Tile> queue;
	protected double g_Score, H_score;
	protected int width, empty, height;
	protected Map arena;
	//protected Path pth;
	
	public AStarSearch(Map arena) {
		arena = new Map(width, height);
		ArrayList<Tile> openSet = new ArrayList<Tile>();
 		
	}
	
	public LinkedList<Tile> searchForPath(Tile start, Tile goal) {
		openSet.clear();
		
		arena.getTiles();
		openSet.add(start);
		while (!openSet.isEmpty()) {
			Tile currentTile = openSet.get(0);	
			if (currentTile == goal) return reconstructPath();
			
			openSet.remove(0);
			closedSet.add(currentTile);
			
			
			
			for (int x = 0; x < arena.getWidth(); x++) {
				for (int y = 0; y < arena.getHeight(); y++) {
					Tile neighbour = arena.getTile(x, y);
					
					if (neighbour == currentTile) break;
					
					double g = calculateManhatten(currentTile);
					double h = calculateManhatten(currentTile);
					
					double f_Score = g + calculateManhatten(goal);
					
				}
			}
			
		}
		return null;
	}
	
	public LinkedList<Tile> reconstructPath() {
		
		LinkedList<Tile> path = new LinkedList();
		//need to do a for loop or til current tile = start.
		for (Tile tile : cameFrom) {
			path.push(tile);
		}
		return path;
		
		
	}
	
	public double calculateManhatten(Tile nextTile) {
		double xDistance = arena.getRobotX() - nextTile.getX();
		double yDistance = arena.getRobotY() - nextTile.getY();
		double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance));		
		return distance;
		
	}
}
