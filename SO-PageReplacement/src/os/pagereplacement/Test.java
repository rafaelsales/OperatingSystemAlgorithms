package os.pagereplacement;

import java.util.ArrayList;
import java.util.List;

import os.pagereplacement.algorithm.FIFO;
import os.pagereplacement.algorithm.LFU;
import os.pagereplacement.algorithm.LRU;
import os.pagereplacement.algorithm.MFU;
import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;

/**
 * Test harness for LRU and FIFO page replacement algorithms
 * 
 * Usage: java [-Ddebug] Test <reference string size> <number of page frames>
 */

public class Test {
	public static void main(String[] args) {
		int referenceStringSize;
		int framesCount;
		try {
			referenceStringSize = Integer.parseInt(args[0]);
			framesCount = Integer.parseInt(args[1]);
		} catch (Exception e) {		
			System.out.println("Argumentos inválidos.");
			System.out.println("1o argumento: tamanho da cadeia de referências");
			System.out.println("2o argumento: quantidade de frames de memória");
			return;
		}
		System.out.println("Tamanho da cadeia de referencias: " + referenceStringSize);
		System.out.println("Quantidade de frames: " + framesCount);
		System.out.println();
		
		PageGenerator pageGenerator = new PageGenerator(referenceStringSize);

		int[] referenceString = pageGenerator.getReferenceString();

		List<ReplacementAlgorithm> replacementAlgorithms = new ArrayList<ReplacementAlgorithm>();
		/** Use either the FIFO or LRU algorithms */
		replacementAlgorithms.add(new FIFO(framesCount));
		replacementAlgorithms.add(new LRU(framesCount));
		replacementAlgorithms.add(new LFU(framesCount));
		replacementAlgorithms.add(new MFU(framesCount));
		replacementAlgorithms.add(new Optimal(framesCount, referenceString));

		for (int i = 0; i < referenceString.length; i++) {
			for (ReplacementAlgorithm replacementAlgorithm : replacementAlgorithms) {
				if (replacementAlgorithm instanceof Optimal) {
					replacementAlgorithm.insert(i);
				} else {
					replacementAlgorithm.insert(referenceString[i]);
				}
			}
		}

		// report the total number of page faults
		for (ReplacementAlgorithm algorithm : replacementAlgorithms) {
			System.out.println(String.format("Quantidade de page faults do %s: %d", algorithm.getName(), algorithm.getPageFaultCount()));
		}
	}
}
