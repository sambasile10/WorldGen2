package graphics;

import java.awt.Graphics;

import javax.swing.JComponent;

import generation.World;

/*
 * JComponent for painting the map
 */

public class MapCanvas extends JComponent {
	
	/*
	 * viewX and viewY should be the center coordinates displayed
	 */
	protected int viewX, viewY, ulX, ulY;
	protected int worldSize;
	protected int windowWidth, windowHeight;
	protected int zoomFactor = 1;
	
	//(technically a pointer) to the World object
	World world;
	
	public MapCanvas(int width, int height, World world) {
		this.world = world;
		this.worldSize = world.getWorldSize();
		this.windowWidth = width;
		this.windowHeight = height;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Draw the canvas pixel by pixel, depending on the view size and zoom
		int mX = 0, mY = 0;
		for(int x = 0; x < (windowWidth / zoomFactor); x += zoomFactor) {
			for(int y = 0; y < (windowHeight / zoomFactor); y += zoomFactor) {
				g.setColor(world.getBiomeMap()[ulX + mX][ulY + mY].getColor());
				g.drawRect(x, y, zoomFactor, zoomFactor);
				mX++;
				mY++;
			}
		}
	}
	
	public void setView(int x, int y) {
		this.viewX = x;
		this.viewY = y;
		repaint();
	}
	
	public void move(int dx, int dy) {
		this.viewX += dx;
		this.viewY += dy;
		
		this.ulX = viewX - (windowWidth / 2);
		this.ulY = viewY - (windowHeight / 2);
		
		/*
		 * Check bounds of the viewfinder
		 */
		if(ulX < 0) {
			this.ulX = 0;
			this.viewX = windowWidth / 2;
		}
		
		if(ulY < 0) {
			this.ulY = 0;
			this.viewY = windowHeight / 2;
		}
		
		if((ulX + windowWidth) > worldSize) {
			this.ulX = worldSize - windowWidth;
			this.viewX = worldSize - (windowWidth / 2);
		}
		
		if((ulY + windowHeight) > worldSize) {
			this.ulY = worldSize - windowHeight;
			this.viewY = worldSize - (windowHeight / 2);
		}
		
		//Call for repaint
		repaint();
	}

}
