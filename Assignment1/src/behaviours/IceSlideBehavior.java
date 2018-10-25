package behaviours;
import lejos.hardware.Button;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.IceSlide;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.screen.LCDRenderer;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;

public class IceSlideBehavior implements Behavior{
	public boolean suppressed;
	private Robot robot;
	
	
	public IceSlideBehavior (Robot robot) {
		this.robot = robot;
	}
	
	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		IceSlide.iceSlide(robot);
	}
	
	public boolean takeControl() {
		return true;
	}
}
