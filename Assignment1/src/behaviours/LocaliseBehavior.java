package behaviours;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;

public class LocaliseBehavior implements Behavior{
	public boolean suppressed;
	private Robot robot;
	
	public LocaliseBehavior (Robot robot) {
		this.robot = robot;
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
