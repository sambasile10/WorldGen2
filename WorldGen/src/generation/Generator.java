package generation;

import java.util.Random;

import ext.SimplexNoise;

public class Generator implements Runnable {
	
	private int hSize, aSize, seed, numOctaves;
	private double persistence;
	private Random random;
	private boolean isInitialized = false;
	
	/*
	 * SimplexNoise variables
	 */
	private SimplexNoise[] octaves;
	private double[] frequencies;
	private double[] amplitudes;
	
	/*
	 * Generates a Generator thread, size is a message of (0 to x+, -x to 0, etc)
	 * The map will be width=x*2 and length=x*2
	 * The seed is used to seed a random number generator
	 * The seed also calculates the other generation values as well
	 */
	public Generator(int hSize, int seed) {
		this.hSize = hSize;
		this.aSize = hSize * 2;
		this.seed = seed;
	}
	
	/*
	 * Set the generation octaves and the persistence value
	 */
	public void setParameters(int numOctaves, double persistence) {
		this.numOctaves = numOctaves;
		this.persistence = persistence;
		
		this.octaves = new SimplexNoise[numOctaves];
		this.frequencies = new double[numOctaves];
		this.amplitudes = new double[numOctaves];
		this.random = new Random(seed);
		
		for (int i = 0; i < numOctaves; i++) {
			octaves[i] = new SimplexNoise(random.nextInt());
			frequencies[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(this.persistence, octaves.length - i);
		}
		
		this.isInitialized = true;
	}
	
	@Override
	public void run() {
		if(isInitialized) {
			int returnCode = this.generate();
		}
	}
	
	private int generate() {
		
	}

}
