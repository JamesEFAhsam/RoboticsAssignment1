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
		this.setRobotPos(0, 0, 0);		//(x, y, heading)
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
	
	public Tile getTile(int heading){
		if(heading == 0){
			return getTile(getRobotX(), getRobotY()+1);
		} else if(heading == 1){
			return getTile(getRobotX()+1, getRobotY());
		} else if(heading == 2){
			return getTile(getRobotX(), getRobotY()-1);
		} else if(heading == 3){
			return getTile(getRobotX()-1, getRobotY());
		}
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
	
	public void updateTile(int x, int y, boolean isEmpty, float percentage){
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
	
	public int getRobotHeading() {
		return robotHeading;
	}

	public void setRobotPos(int robotX, int robotY, int robotHeading) {
		this.robotX = robotX;
		this.robotY = robotY;
		this.robotHeading = robotHeading;
		updateRobotTile();
	}
	
	private void updateRobotTile(){
		this.tiles[robotX][robotY].visit();
	}
	
	public void updateMap(int heading, float F, float L, float R){
		int forwardheading = heading;
		int leftheading = heading-1;
		int rightheading = heading+1;
		
		if(leftheading < 0)
			leftheading = 3;
		
		if(rightheading > 3)
			rightheading = 0;
		
		updateTiles(forwardheading, F);
		updateTiles(leftheading, L);
		updateTiles(rightheading, R);
	}
	
	public void updateTiles(int heading, float tiles){
		int dx = 0, dy = 0;
		
		if(heading == 0)
			dy = 1;
		if(heading == 1)
			dx = 1;
		if(heading == 2)
			dy = -1;
		if(heading == 3)
			dx = -1;
		
		int dW = (int) Math.round(tiles/.25f)+1;
		
		for (int i = 0; i < this.tiles.length; i++) {
			updateTile(getRobotX()+(i*dx), getRobotY()+(i*dy), !(i>=dW), (float) Math.pow(.5, i));
			if(i>=dW)
				break;
		}
	}
	
	public void moveRobotPos(int dX, int dY) {
		this.robotX += dX;
		this.robotY += dY;
		updateRobotTile();
	}
	
	public void moveRobotPos(int heading) {
		if(heading == 0){
			this.robotY += 1;
		} else if(heading == 1){
			this.robotX += 1;
		} else if(heading == 2){
			this.robotY -= 1;
		} else if(heading == 3){
			this.robotX -= 1;
		}
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
	
	public boolean canMove(int heading){
		Tile to = getTile(heading);
		if(to == null)
			return false;
		
		int x = to.getX();
		int y = to.getY();
		if(x < 0 || x >= width)
			return false;
		
		if(y < 0 || y >= height)
			return false;
		
		if(tiles[x][y].getOccupiedBelief() >= 0.95f)
			return false;
		
		return true;
	}
	
	public boolean beenVisited(int heading){
		if(!canMove(heading))
			return false;
		
		Tile tile = getTile(heading);
		
		if(tile == null)
			return false;
		
		if(tile.getVisitAmount() > 0)
			return true;
		
		return false;
	}
}
