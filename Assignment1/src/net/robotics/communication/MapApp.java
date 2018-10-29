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
	JFrame f = new JFrame();
	JPanel jp;
	Graphics g;


	public MapApp() {
		f.setTitle("Simple Drawing");
		f.setSize(500, 500);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);

		jp = new GPanel();
		f.add(jp);
		f.setVisible(true);
}

	public static void main(String[] args) {
		MapApp g1 = new MapApp();
		g1.setVisible(true);
		Graphics gr;
		g1.paintComponentl(10, 10, gr, Robot.current.getMap());
	}



class GPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public GPanel() {
        f.setPreferredSize(new Dimension(500,500));
    }
    
    
    public void paintComponentl(int x, int y, Graphics g, Map map) {
        //rectangle originates at 10,10 and ends at 300, 350
    	
        g.drawRect(10, 10, 300, 350);
        g.drawLine(60, 10, 60, 360);
        g.drawLine(110, 10, 110, 360);
        g.drawLine(160, 10, 160, 360);
        g.drawLine(210, 10, 210, 360);
        g.drawLine(260, 10, 260, 360);
        
        g.drawLine(10, 60, 310, 60);
        g.drawLine(10, 110, 310, 110);
        g.drawLine(10, 160, 310, 160);
        g.drawLine(10, 210, 310, 210);
        g.drawLine(10, 260, 310, 260);
        g.drawLine(10, 310, 310, 310);
        
			for (int i = 0; i < map.getWidth(); i++) {
				for (int j = 0; j < map.getHeight(); i++) {
					Tile tile = map.getTile(i,j);
					float ob = tile.getOccupiedBelief();
					 
					if(ob >= Robot.current._OCCUPIEDBELIEFCUTOFF) {
						g.fillRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					} else {
						g.drawRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					}
					
					if(i == map.getRobotX() && j == map.getRobotY()){
						//Font f = graph.getFont();
						//graph.setFont(Font.getDefaultFont());
						g.drawString("R", x+(tile.getX()*16) , x+(map.getHeight()*16)-(tile.getY()*16));
						//graph.setFont(f);
					} else {
						Font f = g.getFont();
						//graph.setFont(Font.getSmallFont());
						
						g.drawString(tile.getEmpty() + "" + tile.getVisitAmount(), getX()+(tile.getX()*16) + 2, getY()+(map.getHeight()*16)-(tile.getY()*16)+ 4, ob >= Robot.current._OCCUPIEDBELIEFCUTOFF);
						g.setFont(f);
					}
			}
		}

	 }
}


	
	 
	 public void DrawMap(int x, int y,  Map map) {
			for (int i = 0; i < map.getWidth(); i++) {
				for (int j = 0; j < map.getHeight(); i++) {
					Tile tile = map.getTile(i,j);
					float ob = tile.getOccupiedBelief();
					 
					if(ob >= Robot.current._OCCUPIEDBELIEFCUTOFF) {
						g.fillRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					} else {
						g.drawRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					}
					
					if(i == map.getRobotX() && j == map.getRobotY()){
						//Font f = graph.getFont();
						//graph.setFont(Font.getDefaultFont());
						g.drawString("R", x+(tile.getX()*16) , x+(map.getHeight()*16)-(tile.getY()*16));
						//graph.setFont(f);
					} else {
						Font f = g.getFont();
						//graph.setFont(Font.getSmallFont());
						
						g.drawString(tile.getEmpty() + "" + tile.getVisitAmount(), getX()+(tile.getX()*16) + 2, getY()+(map.getHeight()*16)-(tile.getY()*16)+ 4, ob >= Robot.current._OCCUPIEDBELIEFCUTOFF);
						g.setFont(f);
					}
			}
		}

	 }
}
