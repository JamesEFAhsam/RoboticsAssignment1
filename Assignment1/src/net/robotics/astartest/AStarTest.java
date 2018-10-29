package net.robotics.astartest;

import java.util.LinkedList;

import net.robotics.map.AStarSearch;
import net.robotics.map.Map;
import net.robotics.map.Tile;

public class AStarTest {

	public static void main(String[] args) {
		Map map = new Map(6, 7);
		
		map.getTile(1, 0).view(false);
		map.getTile(3, 1).view(false);
		map.getTile(3, 2).view(false);
		
		map.getTile(4, 3).view(false);
		map.getTile(4, 5).view(false);
		map.getTile(3, 4).view(false);
		
		
		
		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 7; y++) {
				if(x == 2 && y == 4)
					continue;
				
				map.getTile(x, y).view(true);
				map.getTile(x, y).view(true);
			}
		}
		
		map.getTile(2, 4).view(false);
		
		AStarSearch search = new AStarSearch(map);
		
		Tile leastKnown = search.getLeastVisitedNode();
		
		System.out.println(leastKnown.getX() + "/" + leastKnown.getY());
		
		LinkedList<Tile> tiles = search.searchForPath(map.getTile(map.getRobotX(), map.getRobotY()), leastKnown);
		
		if(tiles == null){
			System.out.println("No Solution");
			System.exit(0);
		}
		
		
		while (!tiles.isEmpty()) {
			Tile tile = (Tile) tiles.pop();
			System.out.println(tile.getX() + "/" + tile.getY());
		}
		
		
	}

}
