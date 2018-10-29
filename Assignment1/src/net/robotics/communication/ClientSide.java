package net.robotics.communication;

import java.net.*;
import com.google.gson.Gson;
//import lejos.hardware.lcd.Font;
//import lejos.hardware.lcd.GraphicsLCD;
import net.robotics.main.Robot;
import net.robotics.map.Tile;
//import net.robotics.screen.LCDRenderer;
import net.robotics.map.Map;

import java.awt.*;

import javax.sound.midi.ControllerEventListener;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
public class ClientSide  {
	/**
	 * 
	 */
	
	Map map; Font font;
	Graphics2D graph;
	

	TextField txtIPAddress;TextArea messages;Button btnConnect;
	
	public static void main(String[] args) throws IOException {
		String ip = "127.0.0.1"; 
		
		if(args.length > 0)
			ip = args[0];
		
		Socket sock = new Socket(ip, 9090);
		System.out.println("Connected");
		InputStream in = sock.getInputStream();
		DataInputStream dIn = new DataInputStream(in);
		String str = dIn.readUTF();
		//Gson gson = new Gson();
		//str = gson.fromJson(json, Map.class);
		System.out.println(str);
		sock.close();
		
		
	}
	


	 
	 
	 public void DrawMap(int x, int y,  Map map) {
			for (int i = 0; i < map.getWidth(); i++) {
				for (int j = 0; j < map.getHeight(); i++) {
					Tile tile = map.getTile(i,j);
					float ob = tile.getOccupiedBelief();
					 
					if(ob >= Robot.current._OCCUPIEDBELIEFCUTOFF) {
						graph.fillRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					} else {
						graph.drawRect(x+(tile.getX()*16), y+(map.getHeight()*16)-(tile.getY()*16), 16, 16);
					}
					
					if(i == map.getRobotX() && j == map.getRobotY()){
						//Font f = graph.getFont();
						//graph.setFont(Font.getDefaultFont());
						graph.drawString("Fenton", x+(tile.getX()*16) , y+(map.getHeight()*16)-(tile.getY()*16));
						//graph.setFont(f);
					} else {
						Font f = graph.getFont();
						//graph.setFont(Font.getSmallFont());
						
						graph.drawString(tile.getEmpty() + "" + tile.getVisitAmount(), x+(tile.getX()*16) + 2, y+(map.getHeight()*16)-(tile.getY()*16)+ 4);
						graph.setFont(f);
					}
				}
			}
		}

	
}
