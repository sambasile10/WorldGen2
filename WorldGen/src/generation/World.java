package generation;

/*
 * Object that stores the FloatMaps for the world
 * Also stores a 2D array of biomes for drawing
 */

public class World {
	
	private FloatMap elevationMap, temperatureMap;
	private Biome[][] biomeMap;
	private int worldSize, worldSeed;
	
	public World(int worldSeed, FloatMap eMap, FloatMap tMap, Biome[][] biomeMap) {
		this.elevationMap = eMap;
		this.temperatureMap = tMap;
		this.biomeMap = biomeMap;
		this.worldSize = biomeMap.length;
		this.worldSeed = worldSeed;
	}

	public FloatMap getElevationMap() {
		return elevationMap;
	}

	public void setElevationMap(FloatMap elevationMap) {
		this.elevationMap = elevationMap;
	}

	public FloatMap getTemperatureMap() {
		return temperatureMap;
	}

	public void setTemperatureMap(FloatMap temperatureMap) {
		this.temperatureMap = temperatureMap;
	}

	public Biome[][] getBiomeMap() {
		return biomeMap;
	}

	public void setBiomeMap(Biome[][] biomeMap) {
		this.biomeMap = biomeMap;
	}

	public int getWorldSize() {
		return worldSize;
	}

	public void setWorldSize(int worldSize) {
		this.worldSize = worldSize;
	}

	public int getWorldSeed() {
		return worldSeed;
	}

	public void setWorldSeed(int worldSeed) {
		this.worldSeed = worldSeed;
	}

}
