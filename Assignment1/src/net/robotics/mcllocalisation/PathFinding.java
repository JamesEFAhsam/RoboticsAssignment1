package net.robotics.mcllocalisation;

import lejos.robotics.RegulatedMotor; 
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.localization.*; //numbers
import lejos.robotics.mapping.LineMap; 
import lejos.robotics.navigation.*;  
import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.remote.ev3.RMIAudio;
import lejos.remote.ev3.RemoteAudio;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.PathFinder;
import lejos.utility.PilotProps;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PathFinding {
/*	private static final short [] note = { 2349, (115 / 3), 0, (5 / 3), 1760,
			(165 / 3), 0, (35 / 3), 1760, (28 / 3), 0, (13 / 3), 1976,
			(23 / 3), 0, (18 / 3), 1760, (18 / 3), 0, (23 / 3), 1568, (15/ 3),
			0, (25 / 3), 1480, (103 / 3), 0, (18 / 3), 1175, (180 / 3), 0,
			(20 / 3), 1760, (18 / 3), 0, (23 / 3), 1976, (20 / 3), 0, (20/ 3),
			1760, (15 / 3), 0, (25 / 3), 1568, (15 / 3), 0, (25 / 3), 2217,
			(98 / 3), 0, (23 / 3), 1760, (88 / 3), 0, (33 / 3), 1760, (75/ 3),
			0, (5 / 3), 1760, (20 / 3), 0, (20 / 3), 1760, (20 / 3), 0,
			(20 / 3), 1976, (18 / 3), 0, (23 / 3), 1760, (18 / 3), 0, (23/ 3),
			2217, (225 / 3), 0, (15 / 3), 2217, (218 / 3) };
*/

	
	public static void main(String[] args) {
		
// step 1: set up the robot
					
				Wheel leftWheel = WheeledChassis.modelWheel(Motor.B, 4.05).offset(-4.9);
				Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 4.05).offset(4.9);
				Chassis chassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
				
				// step 2: new robot object using the setup
				MovePilot robot = new MovePilot(chassis); 
				//leftWheel.setSpeed(750);
				//robot.setSpeed(750);
				//robot.setAcceleration(1000);
				//rightWheel.setAcceleration(1000);
				robot.setLinearSpeed(500);
				robot.setAngularSpeed(600);
				
// step 3:  Create a map
				Line[] lines = new Line[11]; // six lines inside the map
				//vertical edges
				lines[0] = new Line(0f, 25f, 150f, 25f);// line 1
				lines[1] = new Line(0f, 50f, 150f, 50f); // line 2
				lines[2] = new Line(0f, 75f, 150f, 75f); // line 3
				lines[3] = new Line(0f, 100f, 150f, 100f); // line 4
				lines[4] = new Line(0f, 125f, 150f, 125f); // Line 5
				lines[5] = new Line(0f, 150f, 150f, 150f); // Line 6
				//horizontal edges
				lines[6] = new Line(25f, 0f, 25f, 175f); // line 1 
				lines[7] = new Line(50f, 0f, 50f, 175f); // line 2
				lines[8] = new Line(100f, 0f, 100f, 175f); // line 3
				lines[9] = new Line(125f, 0f, 125f, 175f); // line 4
				lines[10] = new Line(150f, 0f, 150f, 175f); // line 5
				// bounds of the arena
				Rectangle bounds = new Rectangle(0f, 0f, 150f, 175f);
				LineMap myMap = new LineMap(lines, bounds); // add the //bounds to the map
				
// step 4: Use a regular grid of node points. Grid space = 20. //Clearance = 15:
				FourWayGridMesh grid = new FourWayGridMesh(myMap, 25, 2f);
// step 5: Use A* search:
				AstarSearchAlgorithm alg = new AstarSearchAlgorithm();
				
// step 6: Give the A* search algorithm and grid to the PathFinder:
				PoseProvider posep = new OdometryPoseProvider(robot);
				PathFinder pf = new NodePathFinder(alg, grid);
				Waypoint goal = new Waypoint(140, 165);
				while (true) {
					pf.startPathFinding(posep.getPose(), goal);
					robot.forward();
				}
				
				
// step 7: store the location of the robot at a given time
	
				
				
				// new navigator loaded with the robot position, and //path
				//Navigator nav = new Navigator(robot, posep);
				
				
				
				
				//System.out.println("Planning path�"); // displays as //the path is calculated
				
				//nav.goTo(12.5f, 160f); // goto the end location.
				//nav.followPath();
				
				/*for ( int i = 0; i < note .length; i += 2) {
					final short w = note [i + 1];
					final int n = note [i];
					RemoteAudio audios = new RemoteAudio(RMIAudio);
					if (n != 0)
						audios.playTone (n, w * 10, 1);
					try {
						Thread. sleep (w * 10);
					} catch (InterruptedException e) {
					}
				}*/
	}
}
