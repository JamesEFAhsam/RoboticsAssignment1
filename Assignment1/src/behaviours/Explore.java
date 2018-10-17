package behaviours;
import lejos.robotics.subsumption.Behavior;
import Subsumption.RobotBrain;

public class Explore implements Behavior{
	public boolean suppressed;
	private RobotBrain robot;
	
	public Explore (RobotBrain rob) {
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
