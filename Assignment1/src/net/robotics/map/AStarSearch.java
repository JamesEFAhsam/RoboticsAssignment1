package net.robotics.map;
import java.lang.reflect.Array;
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
	public Tile currentTile;
	public int index = 0;
	//protected Path pth;
	
	public AStarSearch(Map arena) {
		arena = new Map(width, height);
		ArrayList<Tile> openSet = new ArrayList<Tile>();
 		
	}
	
	public LinkedList<Tile> searchForPath(Tile start, Tile goal) {
		openSet.clear();
		
		//arena.getTiles();
		openSet.add(start);
		while (!openSet.isEmpty()) {
			Tile currentTile = openSet.get(0);	
			if (currentTile == goal) return reconstructPath();
			
			openSet.remove(0);
			closedSet.add(currentTile);
			
			for (int x = -1; x < 2; x++) {
				for (int y = 1; y < 2; y++) {
					if((x != 0 && y != 0) || (x == y && x == 0))
						continue;
					
					Tile neighbour = arena.getTile((x + currentTile.getX()), (y + currentTile.getY()));
					if (neighbour == currentTile) break;
					double g = calculateManhatten(neighbour);
					double h = calculateManhatten(goal);
					double f_Score = g + h;
					
					 double[] costs = new double[4]; 
					 int i;
					for (i= 0 ; i < 4 ; i++) {
						 
						 costs[index] += f_Score;
						 index++;
					 }
					for (i = 0 ; i < 4 ; i++) {
						if (costs[index] < costs[index++] ) {
							int index = 0;
							closedSet.add(index , neighbour);
							index++;
						}
						
					}		
					
				}
			}
			
		}
		return null;
	}
	
	public LinkedList<Tile> reconstructPath() {
	
		LinkedList<Tile> path = new LinkedList<Tile>();
		//need to do a for loop or till current tile = start.
		for (Tile tile : closedSet) {
			path.push(tile);
		}
		return path;
		
		
	}
	
	public double calculateManhatten(Tile nextTile) {
		double xDistance = arena.getRobotX() - nextTile.getX();
		double yDistance = arena.getRobotY() - nextTile.getY();
		double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );		
		return distance;
		
	}
	
	public double calculateTotalDistance(Tile start, Tile goal) {
		
		double xDistance = start.getX() - goal.getX();
		double yDistance = start.getY() - goal.getY();
		double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );
		return distance;
		
	}
}
