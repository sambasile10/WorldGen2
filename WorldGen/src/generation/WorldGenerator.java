package generation;

import java.util.Random;

import graphics.ImageGenerator;

public class WorldGenerator implements Runnable {

	private int size, seed, numOctaves;
	private double persistence;
	private World world;
	private Random random;
	
	public WorldGenerator(int size, int seed, int numOctaves, double persistence) {
		this.size = size;
		this.seed = seed;
		this.numOctaves = numOctaves;
		this.persistence = persistence;
	}
	
	@Override
	public void run() {
		this.random = new Random(seed);
		int returnCode = this.generateWorld();
	}
	
	private int generateWorld() {
		/*
		 * Generate a mask for the whole world
		 */
		
		MaskGenerator maskGenerator = new MaskGenerator(seed);
		FloatMap circularMask = maskGenerator.getCircularMask(size, 0.9F);
		
		/*
		 * Create 2 SimplexNoiseGenerators for each quadrant
		 */
		
		Thread[] simplexThreads = new Thread[2];
		SimplexNoiseGenerator[] simplexGenerators = new SimplexNoiseGenerator[2];
		for(int st = 0; st < 2; st++) {
			simplexGenerators[st] = new SimplexNoiseGenerator(size / 2, random.nextInt());
			simplexGenerators[st].setParameters(numOctaves, persistence);
			simplexThreads[st] = new Thread(simplexGenerators[st]);
			simplexThreads[st].start();
		}
		
		while(simplexGenerators[0].getStatus() != 1 && simplexGenerators[1].getStatus() != 1) {
			try {
				Thread.currentThread().sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return -3;
			}
		}
		
		FloatMap elevationMap = FloatMap.combineMaps(circularMask, simplexGenerators[0].getFloatMap());
		FloatMap temperatureMap = simplexGenerators[1].getFloatMap();
		Biome[][] biomeMap = new Biome[size][size];
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(Biome biome : Biome.values()) {
					for(Range range : biome.getRange()) {
						if(range.inRange(elevationMap.getAt(x, y), temperatureMap.getAt(x, y))) {
							biomeMap[x][y] = biome;
						}
					}
				}
			}
		}
		
		this.world = new World(this.seed, elevationMap, temperatureMap, biomeMap);
		
		ImageGenerator imageGenerator = new ImageGenerator("world.png", this.world);
		Thread imageThread = new Thread(imageGenerator);
		imageThread.start();
		try {
			imageThread.join();
			return 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
