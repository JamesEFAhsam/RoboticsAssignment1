package net.robotics.communication;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.Gson;

import net.robotics.map.Map;
import net.robotics.main.Robot;

public class MapApp extends JFrame {
	
	private static final int CLOSE = 0;
	public static final int port = 4645;
	private static final long serialVersionUID = 1L;
	
	
	private MapCanvas mapCanvas;
	private TextArea messages;
	private JButton btn;
	private TextField txtIPAddress;
	private Socket socket;
	

	

	public MapApp() {
		this.setTitle("Occupancy Grid Map");
		this.setSize(700, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//this.setLayout(new GridLayout());
		
		JPanel panel = new JPanel();
		panel.setSize(700, 100);
		this.add(panel);
		
		
		
		JLabel title = new JLabel("Fenton LIVE�", JLabel.LEFT);

		btn = new JButton("Connect");
		ButtonListener bl = new ButtonListener();
		btn.addActionListener(bl);
		txtIPAddress = new TextField("192.168.70.64",16);
		messages = new TextArea("status: Started Up");
		messages.setEditable(true);
		panel.add(title);
		//JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT)); //not working when use, could use help figuring out how to use it
		panel.add(btn);
		panel.add(txtIPAddress);
		panel.add(messages);

		panel.add(title);
		//this.add(btn);
		
		JPanel pane = new JPanel();
		pane.setSize(700, 600);
		this.add(pane);
		
		mapCanvas = new MapCanvas();
		pane.add(mapCanvas);
		
		Container contentPane = getContentPane();
		contentPane.add(pane, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.PAGE_START);
		
		
	}
	
	private void run(){
		while(true){
			try{
				Map mappy = getMap();
				mapCanvas.UpdateMap(mappy);
			} catch (Exception exc) {
					messages.setText("status: FAILURE Error establishing connection with server.");
					exc.printStackTrace();
			}
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Map getMap() throws UnknownHostException, IOException{
		if(socket == null){
			//socket = new Socket("192.168.70.64", port);
			socket = new Socket("127.0.0.1", port);
		}
		
		InputStream in = socket.getInputStream();
		DataInputStream dIn = new DataInputStream(in);
		String str = dIn.readUTF();
		return new Gson().fromJson(str, Map.class);
	}

	public static void main(String[] args) {
		MapApp g1 = new MapApp();
		g1.setVisible(true);
		
		g1.run();
	}
	
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();
			messages.setText(command);
			if (command.equals("Connect")) {
				try {
					messages.setText("status: CONNECTED");
					btn.setText("Disconnect");
				} catch (Exception exc) {
					messages.setText("status: FAILURE Error establishing connection with server.");
					exc.printStackTrace();
				}
			}
			else if (command.equals("Disconnect")) {
				disconnect();
			} 
		}
	}
	public void disconnect() {
		try {
			//sendCommand(CLOSE);
			socket.close();
			btn.setText("Connect");
			messages.setText("status: DISCONNECTED");
		} catch (Exception exc) {
			messages.setText("status: FAILURE Error closing connection with server.");
			System.out.println("Error: " + exc);
		}
	}
}
