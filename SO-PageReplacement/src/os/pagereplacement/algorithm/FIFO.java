package os.pagereplacement.algorithm;

/**
 * FIFO (First In, First Out): A implementação consiste apenas em armazenar um inteiro que indica o índice da “cabeça” da fila. Com o uso
 * deste índice simulamos uma fila circular no próprio vetor de frames. A página elegida para substiruição é a que está no frame da “cabeça”
 * da fila. Então o índice da cabeça da fila é atualizado para (índice_cabeca + 1) mod |vetor_frames|.
 * 
 * @author Rafael Sales - rafaelcds@gmail.com
 */
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
