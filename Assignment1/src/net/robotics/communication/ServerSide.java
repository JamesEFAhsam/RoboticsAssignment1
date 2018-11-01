package net.robotics.communication;
import java.net.*;

import com.google.gson.Gson;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;

import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.GraphicsLCD;
import java.io.*;
import net.robotics.screen.LCDRenderer;
import net.robotics.main.Robot;
import net.robotics.map.Map;
import net.robotics.map.Tile;
import net.robotics.screen.*;
//import net.robotics.

public class ServerSide extends Thread{

	public static final int port = 4645;
	public static final String ip = "192.168.70.64";
	//public static Map mapel = new Map(6,7); 
	//LCDRenderer screen;
	//LCD lcd;
	ServerSocket server;

	Graphics2D graph;
	private int delay;
	Socket client;

	public ServerSide(int delay) {
		this.setDaemon(true);
		this.delay = delay;
	}


	public synchronized void start() {
		try {
			server = new ServerSocket(port);
			LCD.drawString("Awaiting client..", 30, 56);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.start();
	}



	@Override
	public void run() {
		
		DataOutputStream out = null;
		Gson gson = new Gson();
		
		while(true){
			

			try{
				if(client == null){
					client = server.accept();
					LCD.drawString("CONNECTED", 30 , 58);
					out = new DataOutputStream(client.getOutputStream());
				}
				
				out.writeUTF(gson.toJson(Robot.current.getMap()));
				out.flush();
			} 
			catch(IOException e){
				System.out.println(e.toString());
			}

			try{
				sleep(delay);
			} catch(Exception e){

			}
		}
	}



	public void startServer() throws IOException {


	}	



}
