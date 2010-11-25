package os.pagereplacement.algorithm;

import java.util.Arrays;

public abstract class ReplacementAlgorithm {

	protected int pageFaultCount;
	protected int pageFrameCount;
	protected int[] frames;

	public ReplacementAlgorithm(int pageFrameSize) {
		if (pageFrameSize < 0) {
			throw new IllegalArgumentException();
		}
		this.pageFrameCount = pageFrameSize;
		this.pageFaultCount = 0;
		frames = new int[pageFrameSize];
		Arrays.fill(frames, -1);
	}

	public int getPageFaultCount() {
		return pageFaultCount;
	}
	
	public abstract void insert(int pageNumber);
	
	protected boolean pageIsAlreadyInFrame(int pageNumber) {
		for (int frame : frames) {
			if (pageNumber == frame) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds a free frame
	 * @return índice de um frame livre ou -1 (caso não exista frame livre)
	 */
	protected int findFreeFrameIndex() {
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] == -1) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Insert the page in a free frame ONLY if it's available
	 * @param pageNumber
	 * @return true if the page has been inserted in a free frame successfully; false otherwise
	 */
	protected boolean tryInsertFreeFrame(int pageNumber) {
		int freeFrameIndex = findFreeFrameIndex();
		if (freeFrameIndex != -1) {
			frames[freeFrameIndex] = pageNumber;
			return true;
		}
		return false;
	}
}
