package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/*
 * JFrame that stores a JCompoment for painting and
 * uses a KeyListener for moving around the map
 */

public class DisplayFrame extends JFrame implements KeyListener {
	
	private MapCanvas mapCanvas;
	protected int keyViewStep = 10;
	
	public DisplayFrame() {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int dx = 0, dy = 0;
		if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
            dx = keyViewStep;
		} else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
            dx = -keyViewStep;
        } else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
            dy = keyViewStep;
        } else if(e.getKeyCode()== KeyEvent.VK_UP) {
            dy = -keyViewStep;
        }
		
		mapCanvas.move(dx, dy);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}

}
