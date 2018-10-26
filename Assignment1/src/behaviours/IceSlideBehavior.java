package behaviours;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.screen.LCDRenderer;
import net.robotics.sensor.ColorSensorMonitor.ColorNames;

public class IceSlideBehavior implements Behavior{
	public boolean suppressed;


	public IceSlideBehavior () {
	}

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		Robot.current.screen.clearScreen();
		Robot.current.screen.writeTo(new String[]{
				"Ice Slide"
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		Button.waitForAnyPress();
		
		suppressed = false;
	}

	public boolean takeControl() {
		return true;
	}
}
