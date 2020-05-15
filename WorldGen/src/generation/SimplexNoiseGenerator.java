package generation;

import java.util.Random;

import ext.SimplexNoiseOctave;

public class SimplexNoiseGenerator implements Runnable {
	
	/*
	 * Generator variables
	 */
	private int size, seed, numOctaves;
	private double persistence;
	private Random random;
	private boolean isInitialized = false;
	
	/*
	 * Generator status
	 * -2 = Generator not initialized
	 * -1 = Generator failed 
	 * 0 = Generator running, not done
	 * 1 = Generator exited successfully
	 */
	private int generatorStatus = -2;
	
	/*
	 * SimplexNoise variables
	 */
	private SimplexNoiseOctave[] octaves;
	private double[] frequencies;
	private double[] amplitudes;
	
	/*
	 * Stored FloatMap
	 */
	private FloatMap floatMap;
	
	/*
	 * Generates a Generator thread, size is a message of (0 to x+, -x to 0, etc)
	 * The seed is used to seed a random number generator
	 * The seed also calculates the other generation values as well
	 */
	public SimplexNoiseGenerator(int size, int seed) {
		this.size = size;
		this.seed = seed;
	}
	
	/*
	 * Set the generation octaves and the persistence value
	 */
	public void setParameters(int numOctaves, double persistence) {
		this.numOctaves = numOctaves;
		this.persistence = persistence;
		
		this.octaves = new SimplexNoiseOctave[numOctaves];
		this.frequencies = new double[numOctaves];
		this.amplitudes = new double[numOctaves];
		this.random = new Random(seed);
		
		for (int i = 0; i < numOctaves; i++) {
			octaves[i] = new SimplexNoiseOctave(random.nextInt());
			frequencies[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(this.persistence, octaves.length - i);
		}
		
		this.isInitialized = true;
	}
	
	@Override
	public void run() {
		if(isInitialized) {
			this.generatorStatus = 0;
			this.floatMap = new FloatMap(size, -1.0F, 1.0F);
			int returnCode = this.getSimplexNoise();
			this.generatorStatus = (returnCode == 0 ? 1 : -1);
		}
	}
	
	public int getStatus() {
		return this.generatorStatus;
	}
	
	public FloatMap getFloatMap() {
		return this.floatMap;
	}
	
	/*
	 * Generates a FloatMap of Simplex Noise between -1.0 and 1.0
	 * Returns 0 if successful or -1 if one or more points failed to generate
	 */
	private int getSimplexNoise() {
		boolean safetySwitch = true;
		for (int x = 0; x < this.size; x++) {
			for (int y = 0; y < this.size; y++) {
				float val = (float) this.getNoise2D(x, y);
				if (val > 1.0F) {
					val = 1.0F;
				}
				if (val < -1.0F) {
					val = -1.0F;
				}
				
				boolean set = this.floatMap.set(x, y, val);
				
				if(!set) {
					safetySwitch = false;
				}
			}
		}

		return (safetySwitch ? 0 : -1);
	}

	private double getNoise2D(int x, int y) {
		double result = 0;
		for (int i = 0; i < octaves.length; i++) {
			result = result + octaves[i].noise(x / frequencies[i], y / frequencies[i]) * amplitudes[i];
		}

		return result;
	}

	private double getNoise3D(int x, int y, int z) {
		double result = 0;
		for (int i = 0; i < octaves.length; i++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, octaves.length - i);

			result = result + octaves[i].noise(x / frequency, y / frequency, z / frequency) * amplitude;
		}

		return result;
	}

}
