package net.robotics.mcllocalisation;

import java.util.ArrayList;

public class ParticleSet {
	private ArrayList<Particle> particleSet;
	private int xCells;
	private int yCells;
	
	public ParticleSet(int xCells, int yCells) {
		particleSet = new ArrayList<Particle>(xCells*yCells*4);
		this.xCells = xCells;
		this.yCells = yCells;
		
		for(int x = 0; x < xCells; x++) {
			for(int y = 0; y < yCells; y++) {
				for(int heading = 0; heading < 4; heading++) {
					particleSet.add(new Particle(x, y, heading));
				}
			}
		}
	}
	
	public ArrayList<Particle> getParticleSet() {
		return particleSet;
	}
	
	
	public Particle getParticle(int index) {
		return particleSet.get(index);
	}
	
}
