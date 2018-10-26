package net.robotics.map;
import java.lang.reflect.Array;
//import net.robotics.map.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import net.robotics.main.Robot;

public class AStarSearch {
	

	private Map arena;
	
	private class Node{
		public Tile t;
		private double g;
		private double h;
		private double f;
		
		private Node parent;
		
		public Node(Tile t){
			this.t = t;
		}
		
		public Node(Tile t, double g, double h, double f){
			this(t);
			this.setH(h);
			this.setG(g);
			this.setF(f);
		}

		public double getG() {
			return g;
		}

		public void setG(double g) {
			this.g = g;
		}

		public double getH() {
			return h;
		}

		public void setH(double h) {
			this.h = h;
		}

		public double getF() {
			return f;
		}

		public void setF(double f) {
			this.f = f;
		}
	}
	
	public AStarSearch(Map arena) {
		this.arena = arena;
 		
	}
	
	public LinkedList<Tile> searchForPath(Tile start, Tile goal) {
		ArrayList<Node> openSet = new ArrayList<Node>();
		ArrayList<Node> closedSet = new ArrayList<Node>();
		
		Node startNode = new Node(start);
		startNode.setG(0);
		openSet.add(startNode);
		
		while(!openSet.isEmpty()){
			Node next = getLeastFScore(openSet);
			openSet.remove(next);
			
			//System.out.print("Chosen " + next.t.getX() + "/" + next.t.getY());
			
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if((x != 0 && y != 0) || (x == y && x == 0))
						continue;
					
					int nX = (next.t.getX()+x);
					int nY = (next.t.getY()+y);
					
					//System.out.print("\n looking at " + x + "/" + y + ", " + nX + "/" + nY + ", " + arena.getWidth() + "/" + arena.getHeight());
					
					if(nX<0 || nX>=arena.getWidth() || nY<0 || nY>=arena.getHeight()){
						//System.out.print(", continued");
						continue;
					}
					
					Node neighbor = new Node(arena.getTile(nX, nY));
					
					if(!arena.canMove(nX, nY))
						continue;
					
					neighbor.parent = next;
					
					if(areNodesEqual(neighbor, new Node(goal))){
						//System.out.print(", chosen\n");
						return reconstructPath(neighbor);
					}
					
					neighbor.setG(next.getG() + calculateManhatten(next, neighbor));
					neighbor.setH(calculateManhatten(neighbor.t, goal));
					neighbor.setF(neighbor.getG()+neighbor.getH());
					
					
					if(isInSet(openSet, neighbor)){
						Node inSet = getInSet(openSet, neighbor);
						if(inSet.getF() < neighbor.getF()){
							//System.out.print(", in open set; f: " + neighbor.getF());
							continue;
						} else {
							openSet.remove(inSet);
						}
					}
					
					if(isInSet(closedSet, neighbor)){
						Node inSet = getInSet(closedSet, neighbor);
						if(inSet.getF() < neighbor.getF()){
							//System.out.print(", in closed set; f: " + neighbor.getF());
							continue;
						}else {
							closedSet.remove(inSet);
						}
					}
					
					//System.out.print(", chosen");
					
					openSet.add(neighbor);
				}
			}
			
			closedSet.add(next);
			//System.out.print(".\n");
		}
		
		return null;
	}
	
	private boolean isInSet(ArrayList<Node> tiles, Node n){
		for (Node tile : tiles) {
			if(areNodesEqual(tile, n))
				return true;
		}
		return false;
	}
	
	private Node getInSet(ArrayList<Node> tiles, Node n){
		for (Node tile : tiles) {
			if(areNodesEqual(tile, n))
				return tile;
		}
		return null;
	}
	
	private boolean areNodesEqual(Node a, Node b){
		return (a.t == b.t);
	}
	
	private Node getLeastFScore(ArrayList<Node> tiles){
		Node highest = tiles.get(0);
		for (Node tile : tiles) {
			if(tile.getF() < highest.getF())
				highest = tile;
		}
		return highest;
	}
	
	private LinkedList<Tile> reconstructPath(Node goal) {
	
		LinkedList<Tile> path = new LinkedList<Tile>();
		
		Node current = goal;
		
		//need to do a for loop or till current tile = start.
		while(current.parent != null){
			path.push(current.t);
			current = current.parent;
		}
			
		return path;
		
		
	}
	
	
	
	private int calculateManhatten(int sX, int sY, int eX, int eY) {
		return Math.abs(sX - eX) + 
				Math.abs(sY - eY);
		
	}
	
	private int calculateManhatten(int sX, int sY, Tile nextTile) {
		return calculateManhatten(sX, sY, nextTile.getX(), nextTile.getY());	
	}
	
	private int calculateManhatten(Tile nextTile, int eX, int eY) {
		return calculateManhatten(nextTile.getX(), nextTile.getY(), eX, eY);		
	}
	
	private int calculateManhatten(Tile nextTile) {
		return calculateManhatten(arena.getRobotX(), arena.getRobotY(), nextTile);
	}
	
	private int calculateManhatten(Tile start, Tile goal) {
		return calculateManhatten(start.getX(), start.getY(), goal);
	}
	
	private int calculateManhatten(int sX, int sY, Node nextTile) {
		return calculateManhatten(sX, sY, nextTile.t);	
	}
	
	private int calculateManhatten(Node nextTile, int eX, int eY) {
		return calculateManhatten(nextTile.t, eX, eY);		
	}
	
	private int calculateManhatten(Node nextTile) {
		return calculateManhatten(arena.getRobotX(), arena.getRobotY(), nextTile);
	}
	
	private int calculateManhatten(Node start, Node goal) {
		return calculateManhatten(start.t, goal.t);
	}
}
