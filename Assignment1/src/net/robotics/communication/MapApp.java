package net.robotics.communication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	
	TextArea messages;
	Button btn;
	TextField txtIPAddress;
	private Socket socket;
	private DataOutputStream outStream;

	public MapApp() {
		this.setTitle("Occupancy Grid Map");
		this.setSize(700, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Map testMap = new Map(6, 7);
		
		testMap.getTile(1, 3).view(false);
		
		jp = new MapCanvas(testMap);
		this.add(jp);
	}

	public static void main(String[] args) {
		MapApp g1 = new MapApp();
		g1.setVisible(true);
	}



	class MapCanvas extends JPanel {
		private static final long serialVersionUID = 1L;
		private static final int CLOSE = 0;
		private Map map;
		private int tileSize = 75;
		private final String name = "FENTON!";


		public MapCanvas(Map map) {
			this.map = map;
			JLabel title = new JLabel("Fenton LIVE™", JLabel.LEFT);
			
			JButton btn = new JButton("Connect to Fenton");
			ButtonListener bl = new ButtonListener();
			btn.addActionListener(bl);
			txtIPAddress = new TextField("127.0.0.1",16);
			messages = new TextArea("status: DISCONNECTED");
		    messages.setEditable(false);
		    this.add(title);
			JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT)); //not working when use, could use help figuring out how to use it
		    this.add(btn);
		    this.add(txtIPAddress);
		    
		    this.add(title);
			//this.add(btn);
			this.setPreferredSize(new Dimension(200,500));
		}

		private void sendCommand(int command){
		    // Send coordinates to Server:
		    messages.setText("status: SENDING command.");
		    try {
		      outStream.writeInt(command);
		    } catch(IOException io) {
		      messages.setText("status: ERROR Problems occurred sending data.");
		    }

		    messages.setText("status: Command SENT.");
		  }
		
		private class ButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
			      String command = e.getActionCommand();
			      if (command.equals("Connect")) {
			        try {
			          Socket socket = new Socket(txtIPAddress.getText(), 9090);
			          DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
			          messages.setText("status: CONNECTED");
			          btn.setLabel("Disconnect");
			        } catch (Exception exc) {
			          messages.setText("status: FAILURE Error establishing connection with server.");
			          System.out.println("Error: " + exc);
			        }
			      }
			      else if (command.equals("Disconnect")) {
			        disconnect();
			      } 
			    }
		}
		public void disconnect() {
		    try {
		      sendCommand(CLOSE);
		      socket.close();
		      btn.setLabel("Connect");
		      messages.setText("status: DISCONNECTED");
		    } catch (Exception exc) {
		      messages.setText("status: FAILURE Error closing connection with server.");
		      System.out.println("Error: " + exc);
		    }
		  }
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					g.setColor(Color.BLUE);
					g.drawRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);
					
					if (x == 0 && y == 0) {
						g.setColor(Color.YELLOW);
						g.fillRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);
					}else if (x == map.getWidth()-1 && y == 0) {
						g.setColor(Color.RED);
						g.fillRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);
					}else if (x == 0 && y == map.getHeight()-1) {
						g.setColor(Color.BLUE);
						g.fillRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);
					}else if (x == map.getWidth()-1 && y == map.getHeight()-1) {
						g.setColor(Color.GREEN);
						g.fillRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);
					}
					
					Tile currentTile = map.getTile(x, y);
					String secline = "Visisted: " + currentTile.getVisitAmount();
					String frsline = "Empty:" + currentTile.getEmpty();
					String thrline = "Visited fully:" + (currentTile.getVisitAmount() - currentTile.getEmpty());
					String forline = "(x,y) =" + "("+currentTile.getX()+","+currentTile.getY()+")";
					String fifline = "O.B.:" +currentTile.getOccupiedBelief();
					
					
					//float obc = 0.65f;
					//float robotLOB = 0.45f;
					//float robotHOB = 0.75f;
					
					if (x == map.getRobotX() && y == map.getRobotY()) {
						g.drawString(name, (x*tileSize)+50+20, (y*tileSize)+100+40);
					}
					
					if (currentTile.getOccupiedBelief() > Map._OCCUPIEDBELIEFCUTOFF) {
							g.setColor(Color.BLACK);
							g.fillRect((x*tileSize)+50, (y*tileSize)+100, tileSize, tileSize);	
					} else {
							g.setColor(Color.BLACK);
							g.drawString(frsline, (x*tileSize)+50+5, (y*tileSize)+100+10);
							g.drawString(secline, (x*tileSize)+50+5, (y*tileSize)+100+25);
							g.drawString(thrline, (x*tileSize)+50+5, (y*tileSize)+100+40);
							g.drawString(forline, (x*tileSize)+50+5, (y*tileSize)+100+55);
							g.drawString(fifline, (x*tileSize)+50+5, (y*tileSize)+100+70);
						
					}
					
					//state, visited full / visited,  occupancy belief, visited amounts
					
					//visited = visited - occupancy
					
					//tile.visit() increments visited
					// tile.view(boolean) increments viewed
					// tile.getOccupiedBelief() returns int visited
					// tile.getVisitAmount()
					// tile.getEmpty() tile.getViewed()
					
					
					
				}
			}
			
			
		}
		
		
		
	}
}
