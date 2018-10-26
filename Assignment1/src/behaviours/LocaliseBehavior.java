package behaviours;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Localisation;
import net.robotics.main.Robot;

public class LocaliseBehavior implements Behavior{
	public boolean suppressed;
	private Localisation localisation;
	
	public LocaliseBehavior () {
		this.localisation = Robot.current.getLocalisation();
	}
	
	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		
		Robot.current.screen.clearScreen();
		Robot.current.screen.writeTo(new String[]{
				"Localisation"
		}, 0, 60, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		suppressed = false;
		Robot.current.getLocalisation().localiseRobot();
	}
	
	public boolean takeControl() {
		
		
		float pConf = localisation.getPosiConfidence();
		float oConf = localisation.getOriConfidence();
		float pThr = localisation._POSITHRESHOLD;
		float oThr = localisation._ORITHRESHOLD;
		
		if (pConf < pThr) {							// We can always localise position, so take control if confidence < threshold
			return true;
		} else if (oConf < oThr) {					// We can only localise against an edge for orientation,
			if (localisation.getEdgePresent()) {	// So check for an edge. If there is one, 
				return true;						// We take control.
			} else {								// Otherwise,
				return false;						// We do not.
			}
		} else {									// If position confidence is not below threshold, 
			return false;							// Do not take control. 
		}
	}
}
