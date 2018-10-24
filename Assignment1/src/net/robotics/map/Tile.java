package net.robotics.map;

public class Tile {
	private float occupiedBelief;
	private int visited;
	private int x, y;
	
	private int empty, viewed;
	
	public Tile(int x, int y){
		this.occupiedBelief = 0.5f;
		this.setX(x);
		this.setY(y);
		this.viewed = 0;
		this.empty = 0;
		this.visited = 0;
		
		//this.view(false);
		//this.view(true);
	}
	
	public void view(boolean isEmpty){
		this.viewed++;
		if(isEmpty)
			this.empty++;
	}
	
	public float getOccupiedBelief(){
		if(visited == 0 && this.viewed != 0f){
			if(this.viewed == 0)
				return 0;
			this.occupiedBelief = this.empty/this.viewed;
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
	
	public int getEmpty(){
		return this.empty;
	}
	
	public int getViewed(){
		return this.viewed;
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
}
