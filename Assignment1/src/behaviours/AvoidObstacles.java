package behaviours;
import lejos.robotics.subsumption.Behavior;
import Subsumption.RobotBrain;

public class AvoidObstacles implements Behavior {
	public boolean suppressed;
	private RobotBrain robot;
	
	public AvoidObstacles (RobotBrain rob) {
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
