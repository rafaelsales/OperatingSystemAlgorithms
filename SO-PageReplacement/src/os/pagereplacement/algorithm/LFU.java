package os.pagereplacement.algorithm;

/**
 * LFU (Least Frequently Used): O algoritmo utiliza um vetor de inteiros de mesmo tamanho que o vetor de frames de memória para armazenar
 * contadores de referências às páginas que estão em memória. Sempre que uma página é carregada em um frame, seja por inserção sem page
 * fault ou por substituição, o contador correspondente é definido para 1. Quando a página já está em um frame, o contador correspondente
 * desta é incrementado. A página elegida para substituição é a que possui menor contador.
 * 
 * @author Rafael Sales - rafaelcds@gmail.com
 */
public class LFU extends ReplacementAlgorithm {

	private int[] framesReferenceCounters;

	public LFU(int pageFrameSize) {
		super(pageFrameSize, "LFU");
		framesReferenceCounters = new int[pageFrameSize];
	}

	@Override
	public int insert(int pageNumber) {
		int frameIndex = tryBasicInsert(pageNumber);
		if (frameIndex != -1) {
			framesReferenceCounters[frameIndex]++;
			return frameIndex;
		}

		// Obtém o índice da página que foi referenciada menos vezes:
		int frameIndexOfLRUPage = 0;
		for (int i = 1; i < framesReferenceCounters.length; i++) {
			if (framesReferenceCounters[i] < framesReferenceCounters[frameIndexOfLRUPage]) {
				frameIndexOfLRUPage = i;
			}
		}

		// Substitui a página menos referenciada pela nova página referenciada:
		int replacedFrameIndex = frameIndexOfLRUPage;
		frames[frameIndexOfLRUPage] = pageNumber;

		// Define o contador de referencias da nova página para 1:
		framesReferenceCounters[frameIndexOfLRUPage] = 1;

		return replacedFrameIndex;
	}

}
