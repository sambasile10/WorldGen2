package rsc;

import generation.GenerationParameters;
import generation.WorldGenerator;

public class WorldGen {
	
	public static void main(String[] args) {
		System.out.println("running");
		
		for(int mO = 8; mO <= 12; mO++) {
			for(double mP = 0.6; mP <= 0.85; mP+= 0.05) {
				for(int tO = 8; tO <= 12; tO++) {
					for(double tP = 0.6; tP <= 0.85; tP+= 0.05) {
						System.out.println("mO-" + mO + "-tO-" + tO + "-mP-" + mP + "-tP-" + tP);
						GenerationParameters genParams = new GenerationParameters(1000, 2359345);
						genParams.setSimplexParameters(mO, tO, mP, tP);
						genParams.setImageExportPath("mO-" + mO + "-tO-" + tO + "-mP-" + mP + "-tP-" + tP + ".png");
						WorldGenerator worldGenerator = new WorldGenerator(genParams);
						Thread generatorThread = new Thread(worldGenerator);
						generatorThread.start();
						
						try {
							generatorThread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		
	}

}
