package os.pagereplacement.algorithm;

import java.util.Arrays;

public abstract class ReplacementAlgorithm {

	public static final int EMPTY_FRAME_VALUE = -1;

	private final String name;
	protected int pageFaultCount;
	protected int pageFrameSize;
	protected int[] frames;


	public ReplacementAlgorithm(int pageFrameSize, String name) {
		this.name = name;
		if (pageFrameSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.pageFrameSize = pageFrameSize;
		this.pageFaultCount = 0;

		frames = new int[pageFrameSize];
		Arrays.fill(frames, -1);
	}

	public int getPageFaultCount() {
		return pageFaultCount;
	}

	public int[] getFrames() {
		return frames;
	}

	public abstract int insert(int pageNumber);

	/**
	 * Se a página ainda não estiver em um frame, incrementa o número de page faults e tenta inserir a página em um frame vazio, se houver
	 * 
	 * @param pageNumber
	 * @return true se a página já estiver em um frame ou se a inserção em um frame vazio ocorreu com sucesso; false caso contrário
	 */
	protected boolean tryBasicInsert(int pageNumber) {
		if (getPageFrameIndex(pageNumber) != -1) {
			return true;
		}
		pageFaultCount++;
		if (tryInsertFreeFrame(pageNumber)) {
			return true;
		}
		return false;
	}

	/**
	 * Obtém o índice de uma página no vetor de frames, caso esteja nele.
	 * 
	 * @param pageNumber
	 * @return índice da página ou -1 caso a página não esteja no vetor de frames
	 */
	protected int getPageFrameIndex(int pageNumber) {
		for (int i = 0; i < frames.length; i++) {
			if (pageNumber == frames[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Procura um frame livre
	 * 
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
	 * Insere a página em um frame livre, caso exista
	 * 
	 * @param pageNumber
	 * @return true se a página foi inserida; false caso contrário
	 */
	protected boolean tryInsertFreeFrame(int pageNumber) {
		int freeFrameIndex = findFreeFrameIndex();
		if (freeFrameIndex != -1) {
			frames[freeFrameIndex] = pageNumber;
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void reset() {
		
	}
}
