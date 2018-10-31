package net.robotics.communication;

public class ServerTest {
	public static void main(String[] args) {
		FakeServer s = new FakeServer(100);
		s.start();
		
		while (true) {
			
			s.changeMap();
			
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
