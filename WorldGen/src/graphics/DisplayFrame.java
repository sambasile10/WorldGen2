package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import generation.World;

/*
 * JFrame that stores a JCompoment for painting and
 * uses a KeyListener for moving around the map
 */

public class DisplayFrame extends JFrame implements KeyListener {
	
	private JPanel panel;
	private MapCanvas mapCanvas;
	private World world;
	protected int keyViewStep = 10;
	private int frameWidth, frameHeight, canvasWidth, canvasHeight;
	
	public DisplayFrame(int frameWidth, int frameHeight, World world) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.canvasWidth = frameWidth - 100;
		this.canvasHeight = frameHeight - 50;
		this.world = world;
		
		this.panel = new JPanel();
		panel.setLayout(null);
		
		this.setTitle("Display of World [seed=" + world.getWorldSeed() + ", size=" + world.getWorldSize() + "]");
		this.setSize(frameWidth, frameHeight);
		this.setResizable(false);
		
		mapCanvas = new MapCanvas(frameWidth - 100, frameHeight - 50, world);
		mapCanvas.setLocation(0, 50);
		mapCanvas.setBounds(0, 50, canvasWidth, canvasHeight);
		panel.add(mapCanvas);
		this.setContentPane(panel);
		this.setEnabled(true);
		this.setVisible(true);
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
