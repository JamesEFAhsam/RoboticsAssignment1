package net.robotics.mcllocalisation;

public class testMCL {
	private static PCParticlePoseProvider particlePP;

	private static int xCells = 6;
	private static int yCells = 6;
	
	private static int[][] obstacleLocations = {
			{2,2},
			{3,2},
			{4,3},
			{5,0}
	};
	
	public static void main(String[] args) {
		KnownMap map = new KnownMap(xCells, yCells, obstacleLocations);
		particlePP = new PCParticlePoseProvider(xCells, yCells, map);
		particlePP.localise();
	}
}
