package net.robotics.map;

import java.util.ArrayList;
import java.util.Collection;

public class Tile {
	private float occupiedBelief;
	private int visited;
	private int x, y;
	//NEW FIELDS
	private float g_score = 0;
	private float h_score = 0;
	private Tile cameFrom = null;
	private ArrayList <Tile> tilesArray = new ArrayList<Tile>();
	static final int FACTOR = 10;
	//
	
	private int empty, viewed;
	
	public Tile(int x, int y){
		this.occupiedBelief = 0.5f;
		this.setX(x);
		this.setY(y);
		this.viewed = 0;
		this.empty = 0;
		this.visited = 0;
		
		this.view(false);
		this.view(true);
	}
	
	public void view(boolean isEmpty){
		this.viewed++;
		if(isEmpty)
			this.empty++;
	}
	
	public float getOccupiedBelief(){
		if(visited == 0 && this.viewed != 0f){
			if(this.empty == 0)
				return 0;
			this.occupiedBelief = this.viewed/this.empty;
		}
		return this.occupiedBelief;
	}
	
	public void visit(){
		this.visited++;
		this.occupiedBelief = 0f;
	}
	
	public int getVisitAmount(){
		return this.visited;
	}

	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	private void setY(int y) {
		this.y = y;
	}
	
	//NEW METHODS
	public Collection <Tile> getTiles() {
		return tilesArray;
	}
	
	public int numberofTiles() {
		return tilesArray.size();
	}
	
	public boolean addNewTile(Tile nextTile) {
		if(tilesArray.contains(nextTile)) {
			return false;
		}
		if(nextTile == this) {
			return false;
		}
		tilesArray.add(nextTile);
		return true;
	}
	
	public boolean removeTile(Tile nextTile) {
		return tilesArray.remove(nextTile);
	}
	
	protected void setH_Score(float h) {
		h_score = h;
	}
	
	public double calculateG(Tile nextTile) {
		double xDistance = this.x - nextTile.x;
		double yDistance = this.y - nextTile.y;
		double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance));		
		return distance;
		
	}
	
	protected double calculateH(Tile targetTile) {
		return calculateG(targetTile);
	}
	
	protected void setG_Score(float g) {
		g_score = g;
	}
	
	protected float getG_Score(){
		return g_score;
	}
	
	protected float getF_Score(){
		return g_score + h_score;
	}
	
	protected Tile getPredecessor() {
		return cameFrom;
	}
	
	protected void setPredecessor(Tile origin) {
		cameFrom = origin;
	}
	//
	
	
}
