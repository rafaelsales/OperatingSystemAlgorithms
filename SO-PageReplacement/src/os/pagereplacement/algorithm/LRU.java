package os.pagereplacement.algorithm;

import java.util.Stack;

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
		Integer frameIndexAtStackBase = framesIndexesStack.remove(framesIndexesStack.size() - 1);
		int replacedFrameIndex = frameIndexAtStackBase;

		// Adiciona a nova página referenciada no topo da pilha:
		frames[replacedFrameIndex] = pageNumber;
		framesIndexesStack.push(pageNumber);

		return replacedFrameIndex;
	}
}
