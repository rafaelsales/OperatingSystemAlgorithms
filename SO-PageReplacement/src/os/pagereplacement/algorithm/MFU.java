package os.pagereplacement.algorithm;

public class MFU extends ReplacementAlgorithm {

	private int[] framesReferenceCounters;

	public MFU(int pageFrameSize) {
		super(pageFrameSize);
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
		int replacedPageNumber = frames[frameIndexOfMFUPage];
		frames[frameIndexOfMFUPage] = pageNumber;

		// Define o contador de referencias da nova página para 1:
		framesReferenceCounters[frameIndexOfMFUPage] = 1;

		return replacedPageNumber;
	}

}
