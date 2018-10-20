package behaviours;

import Subsumption.RobotBrain;
import lejos.robotics.subsumption.Behavior;

public class Move implements Behavior {
	public boolean suppressed;
	private RobotBrain robot;
	
	public Move (RobotBrain rob) {
		robot = rob;
	}
	
	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		suppressed = false;
	}
	
	public boolean takeControl() {
		return true;
	}
}
