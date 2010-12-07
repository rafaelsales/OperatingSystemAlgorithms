package os.pagereplacement.algorithm;

import java.util.Stack;

/**
 * LRU (Least Recently Used): O algoritmo utiliza uma estrutura de pilha para deixar as páginas mais recentemente referenciadas próximas do
 * topo da pilha. Sempre que uma página é solicitada e já está no vetor de frames, esta é movida de sua posição atual na pilha para o topo
 * dela. Quando a página solicitada não está em um frame, a página elegida para substituição é a que está na base da pilha, a qual é
 * removida dela, e então a página solicitada é adicionada no topo da pilha.
 * 
 * @author Rafael Sales - rafaelcds@gmail.com
 */
public class LRU extends ReplacementAlgorithm {

	private Stack<Integer> framesIndexesStack;

	public LRU(int pageFrameSize) {
		super(pageFrameSize, "LRU");
		framesIndexesStack = new Stack<Integer>();
	}

	@Override
	public int insert(int pageNumber) {
		int frameIndex = tryBasicInsert(pageNumber);
		// Obtém o índice da página na pilha (se não estiver, o valor do índice será -1):
		if (frameIndex != -1) {
			// Se a página já está em memória, apenas a reposiciona para o topo da pilha:
			framesIndexesStack.remove((Integer) frameIndex);
			framesIndexesStack.push(frameIndex);
			return frameIndex;
		}
		
		// Obtém o índice da página a ser substituída, isto é, o índice da base da pilha:
		int replacedFrameIndex = framesIndexesStack.remove(0);

		// Substitui a página e adiciona o índice de seu frame no topo da pilha:
		frames[replacedFrameIndex] = pageNumber;
		framesIndexesStack.push(replacedFrameIndex);

		return replacedFrameIndex;
	}
}
