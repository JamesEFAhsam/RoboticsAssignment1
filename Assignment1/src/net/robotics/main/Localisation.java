package net.robotics.main;


import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;
import lejos.hardware.sensor.EV3ColorSensor;
import net.robotics.sensor.ColorSensorMonitor;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;
import lejos.robotics.navigation.Pose;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
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
	
	private float oriConfidence; 	//Orientation confidence. 
	private float posiConfidence;	//Position confidence.
	public final float _ORITHRESHOLD = 0.8f; //	NEEDS CALIBRATING
	public final float _POSITHRESHOLD = 0.7f;// NEEDS CALIBRATING
	
	private final float _CONFIDENCEDEC = 0.05f;
	
	public enum dHeadingPosition {
		Top, Right, Down, Left
	}
	
	private int[][] neighbourOffsets = {
			{0,1}, // Above 	[0]
			{1,0}, // To right	[1]
			{0,-1},// Below 	[2]
			{-1,0},// To left	[3]
	};
	
	public Localisation() {
		oriConfidence = 1.0f;
		posiConfidence = 1.0f;
	}
	
	public void localiseRobot() {
		int initialHeading = Robot.current.getMap().getRobotHeading();
		boolean[] foundEdges = findEdges();
		int nFoundEdges = 0;
		
		//Count how many edges in surroundings
		for (boolean b:foundEdges) {
			 if(b) nFoundEdges++;
		}
		
		// Localisation method depends upon number of edges
		// If edges are opposite one another, we can only localise w.r.t. one axis
		// So we localise using 1 edge instead, and one space. 
		switch(nFoundEdges) {
			case 3:
				localise2Edges(foundEdges);
				break;
			case 2: 
				if ((foundEdges[1]==true && foundEdges[3]==true) || (foundEdges[0]==true && foundEdges[2]==true)) {
					localise1Edge(foundEdges);
					break;
				} else {
					localise2Edges(foundEdges);
					break;
				}
			case 1:
				localise1Edge(foundEdges);
				break;
			case 0:
				localise0Edge(foundEdges);
				break;
		}
	}
	
		// If 2 or more perpendicular edges, use this method
		// Localises X position, Y position, and orientation
		// Localises using 2 edges
		public void localise2Edges(boolean[] foundEdges) {
			Robot.current.screen.writeTo(new String[]{
					"Two+ Edges"
			}, 0, 80, GraphicsLCD.LEFT, Font.getDefaultFont());
			int initialHeading = Robot.current.getMap().getRobotHeading();
			int xLocaliseCell;
			int yLocaliseCell;
			
			if(foundEdges[0] == true) {		// We know the edges are perpendicular, and we have at least 2
				yLocaliseCell = 0;			// So if it's not one, it is the other
			} else yLocaliseCell = 2;
			
			if(foundEdges[1] == true) {
				xLocaliseCell = 1;
			} else xLocaliseCell = 3;
			
			Robot.current.turnToHeading(yLocaliseCell);		// Turn to y cell
			alignWithEdge();								// y position now localised
			Robot.current.turnToHeading(xLocaliseCell);		// turn to x cell
			alignWithEdge();								// x position now localised
			Robot.current.turnToHeading(initialHeading);	// return to initial pose
			
			posiConfidence = 1.0f;			// 100% confidence of position
		}
		
		
		
		// If 1 edge or 2 opposite edges, use this method
		// Localises X position, Y position, and orientation
		// Localises using 1 edge, and 1 space
		public void localise1Edge(boolean[] foundEdges) {
			Robot.current.screen.writeTo(new String[]{
					"One Edge and One Space"
			}, 0, 80, GraphicsLCD.LEFT, Font.getDefaultFont());
			int initialHeading = Robot.current.getMap().getRobotHeading();
			int xLocaliseCell;
			int yLocaliseCell;
			
			for (int i=0; i<4; i++) {
				 if(foundEdges[i]) {									// If edge is present,
					 if (i == 0 || i == 2) {							// and if edge is top or bottom
						 yLocaliseCell = i;								// Set yLocalise cell to it
						 xLocaliseCell = 1;								// Set xLocalise as left or right (both empty)
						 Robot.current.turnToHeading(yLocaliseCell);	// Turn to yLocalise cell and 
						 alignWithEdge();								// Align to edge and localise y position
						 Robot.current.turnToHeading(xLocaliseCell);	// Turn to xLocalise cell and
						 alignGridLine();								// Align to grid line and localise x position
						 Robot.current.turnToHeading(initialHeading);
						 break;
					 } else {											// Otherwise edge is left or right
						 xLocaliseCell = i;								// Set xLocalise cell to it
						 yLocaliseCell = 0;								// Set yLocalise as top or bottom (both empty)
						 Robot.current.turnToHeading(xLocaliseCell);	// turn to xLocalise cell and
						 alignWithEdge();								// Align to edge and localise x position
						 Robot.current.turnToHeading(yLocaliseCell);	// Turn to yLocalise cell
						 alignGridLine();								// Align to grid line and localise y position
						 Robot.current.turnToHeading(initialHeading);
						 break;
					 }
				 }
			}
			posiConfidence = 1.0f;
		}
		
		
		
		// If no edges, use this method
		// Localises X position and Y position
		// Localises using 2 spaces
		public void localise0Edge(boolean[] foundEdges) {
			Robot.current.screen.writeTo(new String[]{
					"2 Spaces"
			}, 0, 80, GraphicsLCD.LEFT, Font.getDefaultFont());
			int initialHeading = Robot.current.getMap().getRobotHeading();
			
			Robot.current.turnToHeading(0);					// Turn to yLocalise cell and 
			alignGridLine();								// Align to grid and localise y position
			Robot.current.turnToHeading(1);					// Turn to xLocalise cell and
			alignGridLine();								// Align to grid line and localise x position
			Robot.current.turnToHeading(initialHeading);
			
			posiConfidence = 1.0f;
		}

	
	/*public boolean localiseOrientation() {
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
	}*/
	
	
	// Return edges next to the robot that can be localised against
	public boolean[] findEdges() {
		boolean[] foundEdges = new boolean[4];
		
		// Check each neighbour
		for(int i = 0; i<4; i++) {
			//Neighbour coordinates
			int nX = Robot.current.getMap().getRobotX() + neighbourOffsets[i][0];
			int nY = Robot.current.getMap().getRobotY() + neighbourOffsets[i][1];
			
			int mWidth = Robot.current.getMap().getWidth();
			int mHeight = Robot.current.getMap().getHeight();
			
			if (nX > mWidth-1 || nX < 0) { 		//check if West or East wall
				foundEdges[i] = true;
			}else if (nY > mHeight-1 || nY < 0) { 	//check if North or South wall
				foundEdges[i] = true;
			}else {
				Tile tile = Robot.current.getMap().getTile(nX,nY);
				// If tile is likely occupied, return true, else return False	
				if (tile.getOccupiedBelief() > Map._OCCUPIEDBELIEFCUTOFF) {
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
		
		pilot.forward();
		while(!Robot.current.bothBumpersPressed()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pilot.stop();
		pilot.travel(-4);
		oriConfidence = 1.0f;
	}
	
	public void alignGridLine() {
		MovePilot pilot = Robot.current.getPilot();
		ColorNames cn;
		pilot.forward();
		do{
			cn = Robot.current.getColorSensor().getCurrentColor();
		}while(cn != ColorNames.BLACK);
		pilot.travel(-13.0f);
	}
	
	public boolean getEdgePresent() {
		boolean[] n = findEdges();
		if (n[0] || n[1] || n[2] || n[3]) {
			return true;
		} else {
			return false;
		}
	}
	
	public float getOriConfidence() {
		return oriConfidence;
	}
	
	public void setOriConfidence(float oriConfidence) {
		this.oriConfidence = oriConfidence;
	}
	
	public float getPosiConfidence() {
		return posiConfidence;
	}
	
	public void setPosiConfidence(float posiConfidence) {
		this.posiConfidence = posiConfidence;
	}
	
	public void decreaseOriConfidence(){
		setOriConfidence(getOriConfidence() - _CONFIDENCEDEC);
	}
	
	public void decreasePosiConfidence(){
		setPosiConfidence(getPosiConfidence() - _CONFIDENCEDEC);
	}
}
