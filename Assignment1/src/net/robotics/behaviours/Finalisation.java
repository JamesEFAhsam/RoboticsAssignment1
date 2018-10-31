

package net.robotics.behaviours;

import java.io.File;

import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
import net.robotics.main.Robot;


public class Finalisation implements Behavior{
	public boolean suppressed;
	

	public void suppress() {
		suppressed = true;
	}

	public void action() {
		File f = new File("tada.wav");
		Robot.current.getAudio().playSample(f,100);
		suppressed = false;

		while(!Button.ESCAPE.isDown()) {
			// Display "finished mapping, press escape to exit", to the side of the map
		}
		
		Robot.current.closeProgram();
	}

	public boolean takeControl() {
		if (Robot.current.getMapFinished()) {
			return true;
		} else {
			return false;
		}
	}
}
