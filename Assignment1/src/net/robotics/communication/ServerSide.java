package net.robotics.communication;
import java.net.*;

import com.google.gson.Gson;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;

//import lejos.hardware.lcd.Font;
//import lejos.hardware.lcd.GraphicsLCD;
import java.io.*;
import net.robotics.screen.LCDRenderer;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.screen.*;
//import net.robotics.

public class ServerSide {
	
	public static final int port = 4645;
	Map mapel = new Map(6,7); 
	LCDRenderer screen;
	Graphics2D graph;
	
	
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(9090, 0, InetAddress.getByName(null));
		System.out.println("Awaiting client..");
		Socket client = server.accept();
		System.out.println("CONNECTED");
		OutputStream out = client.getOutputStream();
		DataOutputStream dOut = new DataOutputStream(out);
		Gson gson = new Gson();
		dOut.writeUTF(gson.toJson(Robot.current.getMap()));
		
		dOut.flush();
		server.close();
	}	
		
	

}
