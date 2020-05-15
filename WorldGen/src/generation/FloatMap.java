package generation;

public class FloatMap {
	
	/*
	 * 2D map of floats between the given range with every (x, y) position
	 */
	
	private float[][] map;
	
	private int size;
	
	private float MIN_VALUE = -1.0F, MAX_VALUE = 1.0F;
	
	//Create FloatMap using the bounds (x, y) of the map
	public FloatMap(int size, float minValue, float maxValue) {
		this.size = size;
		this.map = new float[size][size];
		this.MIN_VALUE = minValue;
		this.MAX_VALUE = maxValue;
	}
	
	//Set the float elevation at a given (x, y)
	public boolean set(int x, int y, float f) {
		if(x >= size || x < 0 || y >= size || y < 0) {
			//Coordnates out of bounds
			return false;
		}
		
		if(f > 1.0F || f < -1.0F) {
			//Float is out of bounds
			return false;
		}
		
		map[x][y] = f;
		return true;
	}
	
	public float getAt(int x, int y) {
		if(x >= size || x < 0 || y >= size || y < 0) {
			//Coordnates out of bounds
			return Float.MIN_NORMAL;
		}
		
		return map[x][y];
	}
	
	public int getSize() {
		return this.size;
	}
	
	public float getMinimum() {
		return this.MIN_VALUE;
	}
	
	public float getMaximum() {
		return this.MAX_VALUE;
	}
	
	public boolean setMap(float[][] fMap) {
		if(fMap.length != this.size) {
			//Sizes don't match, return false
			return false;
		} else {
			this.map = fMap;
			return true;
		}
	}
	
	//Changes noise values from -1.0F to 1.0F into 0.0F to 1.0F
	public void adjustRange(float nmin, float nmax) {
		float[][] scaled = new float[size][size];
		// Loop through all values
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				// Rescale value
				scaled[x][y] = (float) ((float) ((nmax - nmin) * (float) (map[x][y] - MIN_VALUE)) / (float) (MAX_VALUE - MIN_VALUE))
						+ (float) nmin;
			}
		}

		// Set globals
		this.map = scaled;
		this.MIN_VALUE = nmin;
		this.MAX_VALUE = nmax;
	}
	
	//Changes noise values from according to the exponent
	public void redistributeNoise(float exponent) {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				this.set(x, y, (float)Math.pow((double)this.getAt(x, y), (double)exponent));
			}
		}
	}
	
	//Applies a step function to the map where all values are normalized in (x) number of levels
	public void stepFunction(int levels) {
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				this.set(x, y, (float)(Math.pow((double)this.getAt(x, y), (double)levels) / (double)levels));
			}
		}
	}
	
	//Blend together two maps of the SAME SIZE
	public static FloatMap combineMaps(FloatMap map1, FloatMap map2) {
		int mapSize = map1.getSize();
		if(mapSize != map2.getSize()) {
			//Maps are not the same size
			return null;
		}
		
		FloatMap blendMap = new FloatMap(mapSize, map1.getMinimum(), map1.getMaximum());
		for(int x = 0; x < mapSize; x++) {
			for(int y = 0; y < mapSize; y++) {
				blendMap.set(x, y, (map1.getAt(x, y) * map2.getAt(x, y)));
			}
		}
		
		return blendMap;
	}

}
