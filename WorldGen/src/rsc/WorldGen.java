package rsc;

import generation.WorldGenerator;

public class WorldGen {
	
	public static void main(String[] args) {
		WorldGenerator worldGenerator = new WorldGenerator(1000, 2359345, 10, 0.66);
		Thread generatorThread = new Thread(worldGenerator);
		generatorThread.start();
		
		try {
			generatorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
