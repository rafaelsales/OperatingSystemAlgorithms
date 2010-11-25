package os.pagereplacement.algorithm;

public class FIFO extends ReplacementAlgorithm {

	private int fifoHeadIndex;

	public FIFO(int pageFrameSize) {
		super(pageFrameSize);
		fifoHeadIndex = 0;
	}

	@Override
	public int insert(int pageNumber) {
		if (tryBasicInsert(pageNumber)) {
			return -1;
		}
		int replacedPageNumber = frames[fifoHeadIndex];
		frames[fifoHeadIndex] = pageNumber;
		fifoHeadIndex = (fifoHeadIndex + 1) % pageFrameSize;
		return replacedPageNumber;
	}

}
