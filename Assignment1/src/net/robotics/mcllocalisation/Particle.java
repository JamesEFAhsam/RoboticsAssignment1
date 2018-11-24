package net.robotics.mcllocalisation;

public class Particle {
	private int heading;
	private int x;		// x coordinate
	private int y;		// y coordinate
	private float w; 	//importance factor
	
	public Particle(int x, int y, int heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getHeading() {
		return heading;
	}
	
	public void setHeading(int heading) {
		this.heading = heading;
	}
	
	public float getWeight() {
		return w;
	}
	
	public void setWeight(int w) {
		this.w = w;
	}
	
	public void setParticle(int x, int y, int heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
	}
}
