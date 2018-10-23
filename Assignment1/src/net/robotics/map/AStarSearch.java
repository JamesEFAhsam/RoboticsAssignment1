package net.robotics.map;
//import net.robotics.map.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

import net.robotics.main.Robot;

public class AStarSearch {
	
	protected ArrayList<Tile[]> openSet, closedSet, cameFrom;
	public PriorityQueue<Tile> queue;
	protected Tile[] start, goal;
	protected double g_Score, H_score;
	protected int width, empty, height;
	protected Map arena;
	protected Path pth;
	protected Tile[][] grid;
	
	public AStarSearch(Map arena, Tile start, Tile goal) {
		this.start = start;
		this.goal = goal;
		arena = new Map(width, height);
		ArrayList<Tile[]> openSet = new ArrayList<Tile[]>();
		openSet.add(start);
		Tile[][] grid = arena.getTiles();
 		
	}
	
	public void searchForPath() {
		arena.getTiles();
		while (!openSet.isEmpty()) {
			Tile[] currentTile = openSet.get(0);	
			if (currentTile == goal) return reconstructPath();
			
			openSet.remove(0);
			closedSet.add(currentTile);
			
			for (Tile[] neighbour : grid) {
				if (neighbour == currentTile) return;
				
				calculateG(currentTile);
				calculateH(currentTile);
				
				double f_Score = calculateG(currentTile) + calculateH(goal);
				
				
				
				
				
			}
			
		}
	}
	
	public Tile[][] reconstructPath() {
		
		Tile[][] path;
		
		while (camefrom.has(currentTile)) {
			currentTile = camefrom(currentTile);
			path.add(currentTile);
		}
		return path;
		
		
	}
	
	public double calculateG(Tile[] nextTile) {
		double xDistance = Robot.x - nextTile.x;
		double yDistance = Robot.y - nextTile.y;
		double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance));		
		return distance;
		
	}
	
	protected double calculateH(Tile[] targetTile) {
		return calculateG(targetTile);
	}
	
	protected float getF_Score(){
		return g_score + h_score;
	}
	
	
}
