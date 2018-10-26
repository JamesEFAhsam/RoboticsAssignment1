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
		
		AStarSearch search = new AStarSearch(map);
		
		LinkedList<Tile> tiles = search.searchForPath(map.getTile(map.getRobotX(), map.getRobotY()), map.getTile(2, 2));
		
		while (!tiles.isEmpty()) {
			Tile tile = (Tile) tiles.pop();
			System.out.println(tile.getX() + "/" + tile.getY());
		}
	}

}
