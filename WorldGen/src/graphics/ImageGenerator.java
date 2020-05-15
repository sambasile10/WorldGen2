package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import generation.World;

public class ImageGenerator implements Runnable {

	private File imageFile;
	private World world;
	
	public ImageGenerator(String fileName, World world) {
		this.imageFile = new File(fileName);
		this.world = world;
	}
	
	@Override
	public void run() {
		System.out.println(imageFile.getAbsolutePath());
		int returnCode = writeImage();
	}
	
	private int writeImage() {
		BufferedImage image = new BufferedImage(world.getWorldSize(), world.getWorldSize(), BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < world.getWorldSize(); x++) {
			for(int y = 0; y < world.getWorldSize(); y++) {
				if(world.getBiomeMap()[x][y] == null) {
					System.out.println("null error");
					return -2;
				}
				
				image.setRGB(x, y, world.getBiomeMap()[x][y].getColor().getRGB());
			}
		}
		
		try {
			//Write created image to the disk in the PNG format
			ImageIO.write(image, "png", this.imageFile);
			System.out.println("done");
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
