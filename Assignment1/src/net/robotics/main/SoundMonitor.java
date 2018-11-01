package net.robotics.main;

import java.io.File;

import lejos.hardware.Audio;
import lejos.hardware.LED;

public class SoundMonitor extends Thread{
	private Audio audio;
	private File file;
	boolean playNow;
	
	
	public SoundMonitor(Audio audio) {
		this.audio = audio;
	}
	
	public void run() {
		while(true) {
			if(playNow && file != null) {
				audio.playSample(file,100);
				playNow = false;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void playSound(File clip) {
		this.file = clip;
		this.playNow = true;
	}
	
}
