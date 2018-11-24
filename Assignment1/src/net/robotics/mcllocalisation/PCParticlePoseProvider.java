package net.robotics.mcllocalisation;

import java.util.ArrayList;
import java.util.Random;

public class PCParticlePoseProvider {
	private ParticleSet tSet;
	
	private KnownMap map;
	private int xCells;
	private int yCells;
	private boolean debugMode = true;
	
	Random rand = new Random();
	
	private boolean objectSensed;

	
	// Random pose of x, y and heading
	private int[] randomPose = new int[3]; // Our "random" location. This is the unknown we want to find. 
	private int[] poseChange = new int[3];
	
	public PCParticlePoseProvider(int xCells, int yCells, KnownMap map) {
		tSet = new ParticleSet(xCells, yCells);
		
		this.map = map;
		this.xCells = xCells;
		this.yCells = yCells;
		
		ArrayList<Particle> toRemove = new ArrayList<Particle>();
		for (int i = 0; i < tSet.getParticleSet().size(); i++) {	
			Particle currParticle = tSet.getParticle(i);
			if (!map.isPointIn(currParticle.getX(), currParticle.getY()) || !map.notObstacle(currParticle.getX(), currParticle.getY())) {
				toRemove.add(currParticle);
			}	
		}
		tSet.getParticleSet().removeAll(toRemove);

		
		randomPose[0] = 2;
		randomPose[1] = 1;	// THIS IS WHERE WE SET RANDOM POSE
		randomPose[2] = 0;
		
		poseChange[0] = 0;
		poseChange[1] = 0;
		poseChange[2] = 0;
	}
	
	public void localise() {
		printI("Localise");
		while (!goodEstimate()) {
			mCL();
		}
	}
	
	
	private boolean goodEstimate() {
		if (tSet.getParticleSet().size() == 1) {
			printI("Good estimate found!");
			printI("x:" + Integer.toString(tSet.getParticle(0).getX()));
			printI("y:" + Integer.toString(tSet.getParticle(0).getY()));
			printI("h:" + Integer.toString(tSet.getParticle(0).getHeading()));
			
			printI("Random Pose:");
			printI("x:" + Integer.toString(randomPose[0]));
			printI("y:" + Integer.toString(randomPose[1]));
			printI("h:" + Integer.toString(randomPose[2]));
			return true;
		} else if (tSet.getParticleSet().size() == 0){
			printI("Good estimate not found");
			return true;
		} else {
			return false;
		}
	}

	// This MCL is not currently probabilistic. It just eliminates incompatible states. 
	private void mCL() {
		move();
		// Then generate new samples
		// And remove incompatible particle states
		ArrayList<Particle> toRemove = new ArrayList<Particle>();
		for (int i = 0; i < tSet.getParticleSet().size(); i++) {
			Particle currParticle = tSet.getParticle(i);
			
			//double prob = currParticle.getWeight();
			
			currParticle.setX(currParticle.getX() + poseChange[0]);
			currParticle.setY(currParticle.getY() + poseChange[1]);
			currParticle.setHeading(addHeadings(currParticle.getHeading(), poseChange[2]));
			
			if (!map.isPointIn(currParticle.getX(), currParticle.getY()) || !map.notObstacle(currParticle.getX(), currParticle.getY())) {
				toRemove.add(currParticle);
			}
		}
		tSet.getParticleSet().removeAll(toRemove);
		
		printRemainingParticles();
		// Sense
		sense();
		// Then eliminate incompatible particle states. 
		toRemove = new ArrayList<Particle>();
		for (int i = 0; i < tSet.getParticleSet().size(); i++) {
			Particle currParticle = tSet.getParticle(i);
			int x = currParticle.getX();
			int y = currParticle.getY();
			int h = currParticle.getHeading();
			
			if (!(objectInFront(x,y,h) == objectSensed)) {
				toRemove.add(currParticle);
			}
		}
		tSet.getParticleSet().removeAll(toRemove);

		printRemainingParticles();
	}
	
	private void printRemainingParticles() {
		String rx = Integer.toString(randomPose[0]);
		String ry = Integer.toString(randomPose[1]);
		String rh = Integer.toString(randomPose[2]);
		printI("Random pose, x:" + rx + " y:" + ry + " h:" + rh);
		
		for (int i = 0; i < tSet.getParticleSet().size(); i++) {
			Particle currParticle = tSet.getParticle(i);
			
			String sI = Integer.toString(i);
			String x = Integer.toString(currParticle.getX());
			String y = Integer.toString(currParticle.getY());
			String h = Integer.toString(currParticle.getHeading());
			
			
			printI("NO:" + sI + " x:" + x + " y:" + y + " h:" + h);
		}
	}
	
	private void printParticle(Particle particle) {
		String x = Integer.toString(particle.getX());
		String y = Integer.toString(particle.getY());
		String h = Integer.toString(particle.getHeading());
		printI("x:" + x + " y:" + y + " h:" + h);
	}
	

	private void move() {
		printI("Move");
		randomRotate();
		moveForward();
	}
	
	private void randomRotate() {
		poseChange[2] = rand.nextInt((3-1) + 1) + 1;
		randomPose[2] = addHeadings(randomPose[2], poseChange[2]);
	}
	
	private void moveForward() {
		int x = randomPose[0];
		int y = randomPose[1];
		int h = randomPose[2];
		
		if (!objectInFront(x,y,h)) {
			if (h == 0) {
				poseChange[0] = 0;
				poseChange[1] = 1;
			} else if (h == 1) {
				poseChange[0] = 1;
				poseChange[1] = 0;
			} else if (h == 2) {
				poseChange[0] = 0;
				poseChange[1] = -1;
			} else {
				poseChange[0] = -1;
				poseChange[1] = 0;
			}
			randomPose[0] += poseChange[0];
			randomPose[1] += poseChange[1];
		} else {
			poseChange[0] = 0;
			poseChange[1] = 0;
		}
	}
	
	private void sense() {
		printI("Sense");
		int x = randomPose[0];
		int y = randomPose[1];
		int h = randomPose[2];
		
		if (objectInFront(x,y,h)) {
			objectSensed = true;
		} else {
			objectSensed = false;
		}
	}
	
	private boolean objectInFront(int x, int y, int h) {	
		int nX;
		int nY;
		
		if (h == 0) {
			nX = x;
			nY = y+1;
		} else if (h == 1) {
			nX = x+1;
			nY = y;
		} else if (h == 2) {
			nX = x;
			nY = y-1;
		} else {
			nX = x-1;
			nY = y;
		}
		if (nX >= 0 && nX < xCells && nY >=0 && nY < yCells) {
			if (map.getTile(nX, nY).getOccupiedBelief() == 1.0) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
		
	}
	
	public int addHeadings(int h1, int h2) {
		int sum = h1 + h2;
		int newHeading = 0;
		switch(sum) {
			case 0:
			case 4: newHeading = 0;
					break;
			case 1:
			case 5:	newHeading = 1;
					break;
			case 2:
			case 6:	newHeading = 2;
					break;
			case 3:	newHeading = 3;
					break;
		}
		return newHeading;
	}
	
	private void printI(String x) {
		if (debugMode == true) {
			System.out.println(x);
		}
	}
}
