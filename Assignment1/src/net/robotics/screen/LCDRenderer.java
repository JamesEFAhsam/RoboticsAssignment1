package net.robotics.screen;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import net.robotics.map.Map;
import net.robotics.map.Tile;

//Screen Width 178, Height 128; 

public class LCDRenderer{
	private GraphicsLCD lcd;
	
	private Font previousFont;
	
	private byte[] storedScreen;
	
	public LCDRenderer(GraphicsLCD lcd){
		this.lcd = lcd;
		lcd.setContrast(0x60);
	}
	
	public void switchScreenTest(){
		writeTo(new String[]{getWidth()+"",getHeight()+""}, 20, 20);
	}
	
	public void switchScreen(){
		byte[] temp = lcd.getDisplay().clone();
		lcd.clear();
		lcd.bitBlt(storedScreen, getWidth(), getHeight(), 0, 0, 20, 20, getWidth()+20, getHeight()+20, GraphicsLCD.ROP_COPY);
		storedScreen = temp;
	}
	
	public void drawEscapeButton(String content, int x, int y, int width, int height, int arc_diam){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+9, y+7, 0);
		lcd.drawLine(x, y,  width, y); // top line
		lcd.drawLine(x, y,  x, y+height-arc_diam/2); // left line
		lcd.drawLine(x+width, y,  width, y+height/2); // right line
		lcd.drawLine(x+arc_diam/2, y+height,  width-10, y+height); // bottom line
		lcd.drawLine(x+width-10, y+height, width, y+height/2); // diagonal
		lcd.drawArc(x, y+height-arc_diam, arc_diam, arc_diam, 180, 90);
		unsetFont();
	}
	
	public void drawUpButton(String content, int x, int y, int width, int height){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+width/2-lcd.getFont().stringWidth(content)/2, y-lcd.getFont().getHeight()/2+height/2, 0);
		
		lcd.drawLine(x+width/4, y,  x+(3*width)/4, y); // top line
		
		lcd.drawLine(x+(3*width)/4, y,  x+width, y+height/2); // top line
		lcd.drawLine(x+width, y+height/2,  x+(7*width)/8, y+(3*height)/4); // top line
		lcd.drawLine(x+(7*width)/8, y+(3*height)/4,  x+(7*width)/8, y+height); // top line
		
		lcd.drawLine(x+(7*width)/8, y+height,  x+(1*width)/8, y+height); // top line
		
		lcd.drawLine(x+(1*width)/8, y+height,  x+(1*width)/8, y+(3*height)/4); // top line
		lcd.drawLine(x+(1*width)/8, y+(3*height)/4,  x, y+height/2); // top line
		lcd.drawLine(x, y+height/2,  x+(1*width)/4, y); // top line
		
		unsetFont();
	}
	
	public void drawDownButton(String content, int x, int y, int width, int height){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+width/2-lcd.getFont().stringWidth(content)/2, y-lcd.getFont().getHeight()/2+height/2, 0);
		
		lcd.drawLine(x+width/4, y+height,  x+(3*width)/4, y+height); // top line
		
		lcd.drawLine(x+(3*width)/4, y+height,  x+width, y+height/2); // top line
		lcd.drawLine(x+width, y+height/2,  x+(7*width)/8, y+(1*height)/4); // top line
		lcd.drawLine(x+(7*width)/8, y+(1*height)/4,  x+(7*width)/8, y); // top line
		
		lcd.drawLine(x+(7*width)/8, y,  x+(1*width)/8, y); // top line
		
		lcd.drawLine(x+(1*width)/8, y,  x+(1*width)/8, y+(1*height)/4); // top line
		lcd.drawLine(x+(1*width)/8, y+(1*height)/4,  x, y+height/2); // top line
		lcd.drawLine(x, y+height/2,  x+(1*width)/4, y+height); // top line
		
		unsetFont();
	}
	
	public void drawRightButton(String content, int x, int y, int width, int height){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+width/2-lcd.getFont().stringWidth(content)/2, y-lcd.getFont().getHeight()/2+height/2, 0);
		
		lcd.drawLine(x, y,  x, y+height); // top line
		
		lcd.drawLine(x, y+height,  x+width/2, y+height); // top line
		lcd.drawLine(x+width/2, y+height,  x+width, y+height/2); // top line
		
		lcd.drawLine(x+width, y+height/2,  x+width/2, y); // top line
		lcd.drawLine(x+width/2, y,  x, y); // top line
		
		unsetFont();
	}
	
	public void drawLeftButton(String content, int x, int y, int width, int height){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+width/2-lcd.getFont().stringWidth(content)/2, y-lcd.getFont().getHeight()/2+height/2, 0);
		
		lcd.drawLine(x+width, y,  x+width, y+height); // top line
		
		lcd.drawLine(x+width, y+height,  x+width/2, y+height); // top line
		lcd.drawLine(x+width/2, y+height,  x, y+height/2); // top line
		
		lcd.drawLine(x, y+height/2,  x+width/2, y); // top line
		lcd.drawLine(x+width/2, y,  x+width, y); // top line
		
		unsetFont();
	}
	
	public void drawEnterButton(String content, int x, int y, int width, int height){
		setFont(Font.getSmallFont());
		lcd.drawString(content, x+width/2-lcd.getFont().stringWidth(content)/2, y-lcd.getFont().getHeight()/2+height/2, 0);
		
		lcd.drawLine(x+width, y,  x+width, y+height); // top line
		lcd.drawLine(x+width, y+height,  x, y+height); // top line
		lcd.drawLine(x, y+height,  x, y); // top line
		lcd.drawLine(x, y,  x+width, y); // top line

		
		unsetFont();
	}
	
	public void drawMenu(String left, String right, String up, String down, String enter){
		drawUpButton(up, getWidth()/2-45/2, 20, 45, 30);
		drawDownButton(down, getWidth()/2-45/2, 80, 45, 30);
		drawRightButton(right, getWidth()/2+(2*45)/4, 42, 35, 45);
		drawLeftButton(left, getWidth()/2-(5*45)/4, 42, 35, 45);
		drawEnterButton(enter, getWidth()/2-15, 52, 30, 25);
	}
	
	public void clearScreen(){
		lcd.clear();
	}
	
	public void setFont(Font font){
		previousFont = lcd.getFont();
		
		lcd.setFont(font);
	}
	
	public void unsetFont(){
		setFont(previousFont);
	}
	
	public void writeTo(String[] content, int x, int y, int anchor, int yshift, Font font, boolean fresh){
		if(fresh)
			clearScreen();
		
		if(font == null)
			font = Font.getDefaultFont();
		
		
		setFont(font);
		
		
		if(yshift == 0)
			yshift = font.getHeight()+1;
		
		int i = 0;
		for (String string : content) {
			lcd.drawString(string, x, y + (yshift*i), anchor);
			i++;
		}
		
		if(font != null)
			unsetFont();
	}
	
	public void writeTo(String[] content, int x, int y, int yshift, int anchor, boolean fresh){
		writeTo(content, x, y, anchor, yshift, null, fresh);
	}
	
	public void writeTo(String[] content, int x, int y, int anchor, boolean fresh){
		writeTo(content, x, y, anchor, lcd.getFont().getHeight()+1, null, fresh);
	}
	
	public void writeTo(String[] content, int x, int y, int yshift, int anchor, Font font){
		writeTo(content, x, y, anchor, yshift, font, false);
	}
	
	public void writeTo(String[] content, int x, int y, int anchor, Font font){
		writeTo(content, x, y, anchor, font.getHeight()+1, font, false);
	}
	
	public void writeTo(String[] content, int x, int y, int yshift, int anchor){
		writeTo(content, x, y, yshift, anchor, false);
	}
	
	public void writeTo(String[] content, int x, int y, int anchor){
		writeTo(content, x, y, anchor, lcd.getFont().getHeight()+1);
	}
	
	public void writeTo(String[] content, int x, int y){
		writeTo(content, x, y, 0);
	}
	
	public void writeTo(String[] content){
		writeTo(content, getWidth()/2, 0, GraphicsLCD.HCENTER);
	}
	
	public void drawMap(int x, int y, Map map){
		writeTo(new String[]{
				"X: " + map.getRobotX(),
				"Y: " + map.getRobotY(),
		}, 0, 0, GraphicsLCD.LEFT, Font.getDefaultFont());
		
		for (int g = 0; g < map.getWidth(); g++) {
			for (int u = 0; u < map.getHeight(); u++) {
				Tile tile = map.getTile(g,u);
				float ob = tile.getOccupiedBelief();
				if(ob <= 0.5f)
					lcd.setStrokeStyle(lcd.DOTTED);
				else
					lcd.setStrokeStyle(lcd.SOLID);
				
				if(ob >= 0.95f)
					lcd.fillRect(x+(tile.getX()*16), y+(tile.getY()*16), 16, 16);
				else
					lcd.drawRect(x+(tile.getX()*16), y+(tile.getY()*16), 16, 16);
				
				if(g == map.getRobotX() && u == map.getRobotY()){
					Font f = lcd.getFont();
					lcd.setFont(Font.getDefaultFont());
					lcd.drawChar('R', x+(tile.getX()*16) , y+(tile.getY()*16) , GraphicsLCD.VCENTER);
					lcd.setFont(f);
				} else {
					Font f = lcd.getFont();
					lcd.setFont(Font.getSmallFont());
					lcd.drawString("" + Math.round(ob*100), x+(tile.getX()*16) + 2, y+(tile.getY()*16)+ 4, GraphicsLCD.VCENTER);
					lcd.setFont(f);
				}
			}
		}
		
	}
	
	public int getWidth(){
		return lcd.getWidth();
	}
	
	public int getHeight(){
		return lcd.getHeight();
	}
			
}
