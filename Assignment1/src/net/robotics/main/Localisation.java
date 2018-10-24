package net.robotics.main;


import net.robotics.map.Map;
import net.robotics.map.Tile;
import lejos.robotics.navigation.Pose;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import java.util.Arrays;

public class Localisation {
	
	/*
	 * This class is responsible for localising both the position and orientation of the robot. 
	 * The basic idea is to localise position or orientation when either go below a certain threshold.
	 * Localisation w.r.t. orientation can be done against one edge. 
	 * Localisation w.r.t. position can be done against 2 adjacent tiles: 2 edges; 1 edge and 1 space; or 
	 * 2 spaces. 
	 * The position before localisation is always conserved, localisation is then done, then the robot is
	 * returned to its initial position.   
	 */
	
	// Need to add "Position confidence"
	// Need to add "Orient confidence"
	
	private int rX = Robot.current.getMap().getRobotX();
	private int rY = Robot.current.getMap().getRobotY();
	
	public enum dHeadingPosition {
		Top, Right, Down, Left
	}
	
	private int[][] neighbourOffsets = {
			{0,1}, // Above 	[0]
			{1,0}, // To right	[1]
			{0,-1},// Below 	[2]
			{-1,0},// To left	[3]
	};
	
	public void localisePosition() {
		int initialHeading = Robot.current.getMap().getRobotHeading();
		boolean[] foundEdges = findEdges();
		int nFoundEdges = 0;
		
		//Count how many edges in surroundings
		for (boolean b:foundEdges) {
			 if(b) nFoundEdges++;
		}
		
		// Localisation method depends upon number of edges
		switch(nFoundEdges) {
			case 3:
			case 2:
				localisePosition2Edges(foundEdges, nFoundEdges);	// If 2 or 3 edges, localise against 2 edges
				break;
			case 1:
				localisePosition1Edge(foundEdges);	// If 1 edge, localise against 1 edge, one space
				break;
			case 0:
				localisePositionNoEdges(foundEdges);// If 0 edges, localise against 2 spaces
				break;
		}
	}
	
	public void localisePosition2Edges(boolean[] foundEdges, int nFoundEdges) {
		int initialHeading = Robot.current.getMap().getRobotHeading();
		int xLocaliseCell;
		int yLocaliseCell;
		
		// If there are 2 edges and they are opposite each other, use the 1 edge method
		if( nFoundEdges == 2 && ((foundEdges[1]==true && foundEdges[3]==true) || (foundEdges[0]==true && foundEdges[2]==true)) ) {
			localisePosition1Edge(foundEdges);
		} else {
			if(foundEdges[0] == true) {
				yLocaliseCell = 0;
			} else yLocaliseCell = 2;
			
			if(foundEdges[1] == true) {
				xLocaliseCell = 1;
			} else xLocaliseCell = 3;
			
			Robot.current.turnToHeading(yLocaliseCell);
			alignWithEdge();
			Robot.current.turnToHeading(xLocaliseCell);
			alignWithEdge();
			Robot.current.turnToHeading(initialHeading);
		}
	}
	
	public void localisePosition1Edge(boolean[] foundEdges) {
		int initialHeading = Robot.current.getMap().getRobotHeading();
		boolean xLocaliseCellFound = false;
		boolean yLocaliseCellFound = false;
		
		for(int i=0; i<4; i++) {
			if (foundEdges[i]) {
				if(i==1 || i==3) {
					xLocaliseCellFound = true;
					Robot.current.turnToHeading(i);
					alignWithEdge();
					break;
				} else {
					yLocaliseCellFound = true;
					Robot.current.turnToHeading(i);
					alignWithEdge();
					break;
				}
			}
		}
		// If we've already localised against x, localise for y, vice versa
		if (xLocaliseCellFound) {
			// Localise for y using colour. 
		} else {
			// Localise for x using colour. 
		}
	}

	public void localisePositionNoEdges(boolean[] foundEdges) {
		int initialHeading = Robot.current.getMap().getRobotHeading();
		
		// Localise in current heading using colour
		
		// Localise adjacent to current heading using colour
	}
	
	public boolean localiseOrientation() {
		int initialHeading = Robot.current.getMap().getRobotHeading();	// Save initial heading
		boolean[] foundEdges = findEdges();						// Look for edges
		
		if (foundEdges[initialHeading]) {						// Check if we are already facing edge
			alignWithEdge();
			return true;
		} else { 												// Check for edges we aren't facing
			for (int i=0; i<4; i++) {
				if(i == initialHeading) {						// Ignore direction we are facing
					continue;
				}
				if(foundEdges[i]) {
					Robot.current.turnToHeading(i);				// Turn to edge
					alignWithEdge();							// Align against edge
					Robot.current.turnToHeading(initialHeading);// Turn back to initial position
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	// Return edges next to the robot that can be localised against
	public boolean[] findEdges() {
		boolean[] foundEdges = new boolean[4];
		
		// Check each neighbour
		for(int i = 0; i<4; i++) {
			//Neighbour coordinates
			int nX = rX + neighbourOffsets[i][0];
			int nY = rY + neighbourOffsets[i][1];
			
			int mWidth = Robot.current.getMap().getWidth();
			int mHeight = Robot.current.getMap().getHeight();
			
			if (nX > mWidth-1 || nX < 0) { 		//check if West or East wall
				foundEdges[i] = true;
			}else if (nY > mHeight-1 || nY < 0) { 	//check if North or South wall
				foundEdges[i] = true;
			}else {
				Tile tile = Robot.current.getMap().getTile(nX,nY);
				// If tile is likely occupied, return true, else return False	
				if (tile.getOccupiedBelief() > Robot.current._OCCUPIEDBELIEFCUTOFF) {
					foundEdges[i] = true;
				} else {
					foundEdges[i] = false;
				}
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
	
	public boolean getEdgePresent() {
		boolean[] n = findEdges();
		if (n[0] || n[1] || n[2] || n[3]) {
			return true;
		} else {
			return false;
		}
	}

}
