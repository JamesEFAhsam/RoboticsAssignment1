package behaviours;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;

public class IceSlide implements Behavior{
	public boolean suppressed;
	private Robot robot;
	
	public IceSlide (Robot robot) {
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
