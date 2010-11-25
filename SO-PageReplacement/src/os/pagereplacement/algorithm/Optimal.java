package os.pagereplacement.algorithm;

public class Optimal extends ReplacementAlgorithm {

	private final int[] pageReferenceString;

	public Optimal(int pageFrameSize, int[] pageReferenceString) {
		super(pageFrameSize);
		this.pageReferenceString = pageReferenceString;
	}

	@Override
	public int insert(int referencedPageIndex) {
		int pageNumber = pageReferenceString[referencedPageIndex];
		if (tryBasicInsert(pageNumber)) {
			return -1;
		}

		int[] frameCopy = frames.clone();
		int indexLastFrameFound = 0; // Elege o 1o frame para substituição caso não nenhum frame será referenciado adiante
		int framesFoundCount = 0;

		// Procura pela página que será referenciada o mais adiante possível:
		for (int i = referencedPageIndex + 1; i < pageReferenceString.length; i++) {
			// Verifica se a página está em algum frame:
			for (int j = 0; j < frameCopy.length; j++) {
				if (pageReferenceString[i] == frameCopy[j]) {
					framesFoundCount++;
					indexLastFrameFound = j;
					frameCopy[j] = -1;
					break;
				}
			}

			/*
			 * Pára a análise do vetor de referencias caso todos os frames da memória já tiverem sido analisados:
			 */
			if (framesFoundCount == pageFrameSize) {
				break;
			}
		}
		int replacedPageIndex = indexLastFrameFound;
		frames[indexLastFrameFound] = pageNumber;
		return replacedPageIndex;
	}
}