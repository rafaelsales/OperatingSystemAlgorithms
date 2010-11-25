package os.pagereplacement.algorithm;

public class LRU extends ReplacementAlgorithm {

	private int stackTopIndex;

	public LRU(int pageFrameSize) {
		super(pageFrameSize, "LRU");
		stackTopIndex = 0;
	}

	@Override
	public int insert(int pageNumber) {
		if (tryBasicInsert(pageNumber)) {
			return -1;
		}
		// Obtém o índice da página na pilha (se não estiver, o valor do índice será -1):
		int pageFrameIndex = getPageFrameIndex(pageNumber);
		
		// Se a página já está em memória, apenas a reposiciona para o topo da pilha:
		if (pageFrameIndex != -1) {
			int iAcima = stackTopIndex;
			// Desloca as páginas do topo da pilha até a página referenciada
			while (iAcima != pageFrameIndex) {
				int iAbaixo = iAcima;
				iAcima = (iAcima + pageFrameSize - 1) % pageFrameSize;
				frames[iAbaixo] = frames[iAcima];
			}
			frames[stackTopIndex] = pageNumber;
			return -1;
		} else {
			// Obtém o índice da página a ser substituída, isto é, o índice da base da pilha (equivalente ao novo topo):
			stackTopIndex = (stackTopIndex + 1) % pageFrameSize;
			int replacedPageIndex = stackTopIndex;
	
			// Adiciona a nova página referenciada no topo da pilha:
			frames[stackTopIndex] = pageNumber;
	
			return replacedPageIndex;
		}
	}

}
