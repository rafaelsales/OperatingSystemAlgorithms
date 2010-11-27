package os.pagereplacement.algorithm;

public class FIFO extends ReplacementAlgorithm {

	private int fifoHeadIndex;

	public FIFO(int pageFrameSize) {
		super(pageFrameSize, "FIFO");
		fifoHeadIndex = 0;
	}

	@Override
	public int insert(int pageNumber) {
		int frameIndex = tryBasicInsert(pageNumber);
		if (frameIndex != -1) {
			return frameIndex;
		}
		int replacedFrameIndex = fifoHeadIndex;
		frames[fifoHeadIndex] = pageNumber;
		fifoHeadIndex = (fifoHeadIndex + 1) % pageFrameSize;
		return replacedFrameIndex;
	}
	
}
