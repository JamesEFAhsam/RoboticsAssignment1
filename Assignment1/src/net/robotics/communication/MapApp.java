package net.robotics.communication;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.main.Robot;

public class MapApp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel jp;
	Graphics g;


	public MapApp() {
		this.setTitle("Simple Drawing");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Map testMap = new Map(5, 5);
		testMap.getTile(1, 3).view(false);
		
		jp = new MapCanvas(testMap);
		this.add(jp);
	}

	public static void main(String[] args) {
		MapApp g1 = new MapApp();
		g1.setVisible(true);
	}



	class MapCanvas extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Map map;


		public MapCanvas(Map map) {
			this.map = map;
			this.setPreferredSize(new Dimension(500,500));
		}


		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// TODO Auto-generated method stub
			
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					g.setColor(Color.BLUE);
					g.drawRect(x*32, y*32, 32, 32);
				}
			}
			
			
		}
		
		
		
	}
}
