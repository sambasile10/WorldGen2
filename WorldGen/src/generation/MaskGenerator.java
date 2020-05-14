package generation;

import ext.SimplexNoise;

public class MaskGenerator implements Runnable {

	
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
		
	}
	
	/*
	 * Returns a FloatMap of Simplex Noise between -1.0 and 1.0
	 * Requires the size of the map to be generated, a seed for the generator
	 * Requires the octaves and persistance value
	 */
	public static FloatMap getSimplexNoise(int size, int seed, int octaves, double persistence) {
		SimplexNoise noise = new SimplexNoise(octaves, persistence, seed);

		float[][] map = new float[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				float val = (float) noise.getNoise2D(x, y);
				if (val > 1.0F) {
					val = 1.0F;
				}
				if (val < -1.0F) {
					val = -1.0F;
				}
				map[x][y] = val;
			}
		}

		FloatMap fmap = new FloatMap(-1, 1, size);
		fmap.setMap(map);
		return fmap;
	}

}
