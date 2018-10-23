package net.robotics.main;


import net.robotics.map.Map;
import net.robotics.map.Tile;
import lejos.robotics.navigation.Pose;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;

public class Localisation {
	
	//This can be implemented in the behavior class. Need to keep counters of turns and movements. 
	/*public void localiseRobot(Robot robot) {
		localisePosition(robot);
		localiseOrientation(robot);
	}*/
	
	
	public void localisePosition(Robot robot) {
		
	}
	
	public void localiseOrientation() {
		Pose initialPose = Robot.current.getOpp().getPose();
		// Look for suitable edge, then align with it
			boolean[] foundEdges = findEdges();
			alignWithEdge();
	}
	
	// Returns edges next to the robot that can be localised against
	public boolean[] findEdges() {
		boolean[] foundEdges = new boolean[4];
		int rX = Robot.current.getMap().getRobotX();
		int rY = Robot.current.getMap().getRobotY();
		
		int[][] neighbourOffsets = {
				{0,1}, // Above 	[0]
				{1,0}, // To right	[1]
				{0,-1},// Below 	[2]
				{-1,0},// To left	[3]
				
		};
		// Check each neighbour
		for(int i = 0; i<4; i++) {
			//Neighbour coordinates
			int nX = rX + neighbourOffsets[i][0];
			int nY = rY + neighbourOffsets[i][1];
			
			if (nX > 6 || nX < 0) { 		//check if L or R wall
				foundEdges[i] = true;
			}else if (nY > 7 || nY < 0) { 	//check if T or B wall
				foundEdges[i] = true;
			}else {
				Tile tile = Robot.current.getMap().getTile(nX,nY);
				// If tile is occupied, return true, else return False				
			}
		}
		return foundEdges;
	}
	
	public void alignWithEdge() {
		MovePilot pilot = Robot.current.getPilot();
		float[] leftSample = Robot.current.getLeftSample();
		float[] rightSample = Robot.current.getRightSample();
		SampleProvider leftTouch = Robot.current.getLeftTouch();
		SampleProvider rightTouch = Robot.current.getRightTouch();
		
		pilot.forward();
		while(leftSample[0] < 0.9 && rightSample[0] < 0.9) {
			leftTouch.fetchSample(leftSample, 0);
	    	rightTouch.fetchSample(rightSample, 0);
		}
		pilot.stop();
		pilot.travel(-4);
	}
	
	private boolean getEdgePresent() {
		boolean[] n = findEdges();
		if (n[0] || n[1] || n[2] || n[3]) {
			return true;
		} else {
			return false;
		}
	}

	
	
	
}
