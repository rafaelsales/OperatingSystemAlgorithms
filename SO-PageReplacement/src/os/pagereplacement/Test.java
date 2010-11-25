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
		PageGenerator pageGenerator = new PageGenerator(new Integer(args[0]).intValue());

		int[] referenceString = pageGenerator.getReferenceString();

		List<ReplacementAlgorithm> replacementAlgorithms = new ArrayList<ReplacementAlgorithm>();
		/** Use either the FIFO or LRU algorithms */
		ReplacementAlgorithm fifo = new FIFO(new Integer(args[1]).intValue());
		ReplacementAlgorithm lru = new LRU(new Integer(args[1]).intValue());
		ReplacementAlgorithm lfu = new LFU(new Integer(args[1]).intValue());
		ReplacementAlgorithm mfu = new MFU(new Integer(args[1]).intValue());
		ReplacementAlgorithm optimal = new Optimal(new Integer(args[1]).intValue(), referenceString);
		replacementAlgorithms.add(fifo);
		replacementAlgorithms.add(lru);
		replacementAlgorithms.add(lfu);
		replacementAlgorithms.add(mfu);
		replacementAlgorithms.add(optimal);

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
		System.out.println("LRU faults = " + lru.getPageFaultCount());
		System.out.println("FIFO faults = " + fifo.getPageFaultCount());
		System.out.println("LFU faults = " + lfu.getPageFaultCount());
		System.out.println("MFU faults = " + mfu.getPageFaultCount());
		System.out.println("OPTIMAL faults = " + optimal.getPageFaultCount());
	}
}
