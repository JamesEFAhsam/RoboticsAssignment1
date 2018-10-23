package net.robotics.main;


import net.robotics.map.Map;
import lejos.robotics.navigation.Pose;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;

public class Localisation {
	
	public void localiseRobot(Robot robot) {
		localisePosition(robot);
		localiseOrientation(robot);
	}
	
	public void localisePosition(Robot robot) {
		
	}
	
	public void localiseOrientation(Robot robot) {
		Pose initialPose = robot.getOpp().getPose();
		// Look for suitable edge, then align with it
			boolean[] foundEdges = findEdges(robot);
			alignWithEdge(robot);
	}
	
	// Returns edges next to the robot that can be localised against
	public boolean[] findEdges(Robot robot) {
		boolean[] foundEdges = new boolean[4];
		int rX = robot.getMap().getRobotX();
		int rY = robot.getMap().getRobotY();
		
		int[][] neighbourOffsets = {
				{0,1}, // Above
				{0,-1},// Below
				{-1,0},// To left
				{1,0}, // To right
		};
		// Check each neighbour
		for(int i = 0; i<4; i++) {
			if (rX + neighbourOffsets[i][0] > 6 || rX + neighbourOffsets[i][0] < 0) { 		//check if y borders
				foundEdges[i] = true;
			}else if (rY + neighbourOffsets[i][1] > 7 || rY + neighbourOffsets[i][1] < 0) { //check if x borders
				foundEdges[i] = true;
			}else {
				
			}
		}
		return foundEdges;
	}
	
	public void alignWithEdge(Robot robot) {
		MovePilot pilot = robot.getPilot();
		float[] leftSample = robot.getLeftSample();
		float[] rightSample = robot.getRightSample();
		SampleProvider leftTouch = robot.getLeftTouch();
		SampleProvider rightTouch = robot.getRightTouch();
		
		pilot.forward();
		while(leftSample[0] < 0.9 && rightSample[0] < 0.9) {
			leftTouch.fetchSample(leftSample, 0);
	    	rightTouch.fetchSample(rightSample, 0);
		}
		pilot.stop();
		pilot.travel(-5);
	}
	
	
	
}
