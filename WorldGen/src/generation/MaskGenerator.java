package generation;

import java.util.Random;

import ext.SimplexNoiseOctave;

public class MaskGenerator implements Runnable {

	private int seed;
	private Random random;
	
	public MaskGenerator(int seed) {
		this.seed = seed;
		this.random = new Random(seed);
	}
	
	@Override
	public void run() {
		
	}
	
	
	/*
	 * Returns a circular mask with a weaker or a stronger dropoff according to the factors
	 * Good for generating islands
	 * modc = multiplier for distance from center
	 */
	public FloatMap getCircularMask(int size, float modc) {
		FloatMap mask = new FloatMap(size, 0.0F, 1.0F);
		int center = (int) (size / 2);
		float max_width = size * 0.5f - 10.0f;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				float distance = (float) (Math.hypot(Math.abs((center - y) * modc), Math.abs((center - x) * modc)));
				float delta = distance / max_width;
				float gradient = delta * delta;
				float val = 1.0F - gradient;
				if (val < 0.0F) {
					val = 0.0F;
				}
				
				mask.set(x, y, val);
			}
		}

		return mask;
	}
	
	
	/*
	 * Returns a noise-derived falloff map to randomize masks further
	 * Requires the FloatMap for the mask to be modified and a float for the
	 * maximum value where the mask will be blending with noise
	 * 
	 * Mask must have a range of 0.0 to 1.0
	 */
	public FloatMap getFalloffNoise(FloatMap mask, float maximumValue) {
		//Create SimplexNoiseGenerator
		SimplexNoiseGenerator noiseGenerator = new SimplexNoiseGenerator(mask.getSize() / 2, this.seed);
		noiseGenerator.setParameters(10, 0.66);
		
		//Start generator as new thread
		Thread generatorThread = new Thread(noiseGenerator);
		generatorThread.start();
		
		//Wait for generator to finish
		try {
			generatorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		FloatMap noiseMap;
		
		//Check if the generator succeeded
		if(noiseGenerator.getStatus() == 1) {
			noiseMap = noiseGenerator.getFloatMap();
		} else {
			//Generation thread failed
			return mask;
		}
		
		//Combine noise with mask if the value of the mask is less than the maximum given
		float[][] blendMap = new float[mask.getSize()][mask.getSize()];
		for(int x = 0; x < mask.getSize(); x++) {
			for(int y = 0; y < mask.getSize(); y++) {
				if(mask.getAt(x, y) <= maximumValue) {
					blendMap[x][y] = (mask.getAt(x, y) * noiseMap.getAt(x, y));
				} else {
					blendMap[x][y] = mask.getAt(x, y);
				}
			}
		}
		
		//Return blended FloatMap
		FloatMap fbMap = new FloatMap(mask.getSize(), 0.0F, 1.0F);
		fbMap.setMap(blendMap);
		return fbMap;
	}

}
