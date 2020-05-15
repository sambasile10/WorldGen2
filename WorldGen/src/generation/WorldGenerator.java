package generation;

import java.util.Random;

import graphics.ImageGenerator;

public class WorldGenerator implements Runnable {

	private int size, seed;
	private GenerationParameters genParams;
	private World world;
	private Random random;
	
	public WorldGenerator(GenerationParameters genParams) {
		this.genParams = genParams;
		this.size = genParams.getWorldSize();
		this.seed = genParams.getWorldSeed();
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
		FloatMap circularMask = maskGenerator.getCircularMask(size, 0.8F);
		
		/*
		 * Create 2 SimplexNoiseGenerators for each quadrant
		 */
		
		Thread[] simplexThreads = new Thread[2];
		SimplexNoiseGenerator[] simplexGenerators = new SimplexNoiseGenerator[2];
		simplexGenerators[0] = new SimplexNoiseGenerator(size, random.nextInt());
		simplexGenerators[0].setParameters(genParams.getElevationOctaves(), genParams.getElevationPersistence());
		simplexThreads[0] = new Thread(simplexGenerators[0]);
		simplexThreads[0].start();
		
		simplexGenerators[1] = new SimplexNoiseGenerator(size, random.nextInt());
		simplexGenerators[1].setParameters(genParams.getMoistureOctaves(), genParams.getMoisturePersistence());
		simplexThreads[1] = new Thread(simplexGenerators[1]);
		simplexThreads[1].start();
		
		try {
			simplexThreads[0].join();
			simplexThreads[1].join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		simplexGenerators[0].getFloatMap().adjustRange(0.0F, 1.0F);
		FloatMap elevationMap = FloatMap.combineMaps(circularMask, simplexGenerators[0].getFloatMap());
		FloatMap temperatureMap = simplexGenerators[1].getFloatMap();
		temperatureMap.adjustRange(0.0F, 1.0F);
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
				
				if(biomeMap[x][y] == null) {
					System.out.println("biome null");
				}
			}
		}
		
		this.world = new World(this.seed, elevationMap, temperatureMap, biomeMap);
		
		ImageGenerator imageGenerator = new ImageGenerator(genParams.getImageExportPath(), this.world);
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
