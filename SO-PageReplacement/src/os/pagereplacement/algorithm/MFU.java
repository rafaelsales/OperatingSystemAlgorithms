package os.pagereplacement.algorithm;

public class MFU extends ReplacementAlgorithm {

	private int[] framesReferenceCounters;

	public MFU(int pageFrameSize) {
		super(pageFrameSize, "MFU");
		framesReferenceCounters = new int[pageFrameSize];
	}

	@Override
	public int insert(int pageNumber) {
		if (tryBasicInsert(pageNumber)) {
			framesReferenceCounters[getPageFrameIndex(pageNumber)]++;
			return -1;
		}

		// Obtém o índice da página que foi referenciada mais vezes:
		int frameIndexOfMFUPage = 0;
		for (int i = 1; i < framesReferenceCounters.length; i++) {
			if (framesReferenceCounters[i] > framesReferenceCounters[frameIndexOfMFUPage]) {
				frameIndexOfMFUPage = i;
			}
		}

		// Substitui a página mais referenciada pela nova página referenciada:
		int replacedPageIndex = frameIndexOfMFUPage;
		frames[frameIndexOfMFUPage] = pageNumber;

		// Define o contador de referencias da nova página para 1:
		framesReferenceCounters[frameIndexOfMFUPage] = 1;

		return replacedPageIndex;
	}

}
