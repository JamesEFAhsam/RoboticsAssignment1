package net.robotics.map;

public class Map {
	private Tile[][] tiles;
	private int width, height;
	private int robotX, robotY;
	private int robotHeading;
	
	public Map(int width, int height){
		this.setWidth(width);
		this.setHeight(height);
		this.setTiles(width, height);
		this.setRobotPos(0, 0);
		updateRobotTile();
	}
	

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Tile[] getTiles(int dimension){
		return tiles[dimension];
	}
	
	public Tile getTile(int x, int y){
		if(x >= 0 && x < width && y >= 0 && y < height)
			return tiles[x][y];
		return null;
	}
	
	private void setTiles(int width, int height) {
		this.tiles = new Tile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.tiles[x][y] = new Tile(x, y);
			}
		}
	}
	
	public void updateTile(int x, int y, boolean isEmpty){
		Tile tile = getTile(x, y);
		if(tile == null)
			return;
		
		tile.view(isEmpty);
	}
	

	private void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
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

	public int getRobotX() {
		return robotX;
	}
	
	public int getRobotY() {
		return robotY;
	}
	
	public void setRobotHeading(int robotHeading) {
		this.robotHeading = robotHeading;
	}
	
	public int getRobotHeading() {
		return robotHeading;
	}

	public void setRobotPos(int robotX, int robotY) {
		this.robotX = robotX;
		this.robotY = robotY;
		updateRobotTile();
	}
	
	private void updateRobotTile(){
		this.tiles[robotX][robotY].visit();
	}
	
	public void moveRobotPos(int dX, int dY) {
		this.robotX += dX;
		this.robotY += dY;
		updateRobotTile();
	}
	
	public boolean canMove(int x, int y){
		if(x < 0 || x >= width)
			return false;
		
		if(y < 0 || y >= height)
			return false;
		
		if(tiles[x][y].getOccupiedBelief() >= 0.95f)
			return false;
		
		return true;
	}
}
