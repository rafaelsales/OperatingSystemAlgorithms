package os.pagereplacement.gui;

import javax.swing.JPanel;

import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;


@SuppressWarnings("serial")
public class AlgorithmPanel extends JPanel {
	
	private ReplacementAlgorithm replacementAlgorithm;

	public AlgorithmPanel(ReplacementAlgorithm replacementAlgorithm) {
		this.replacementAlgorithm = replacementAlgorithm;		
	}

	/**
	 * Solicita a adição da página ao algorítmo
	 * @param pageNumber - número da página
	 * @param pageIndex - índice na página no vetor de frames
	 */
	public void insert(int pageNumber, int pageIndex) {
		/* Caso seja o algorítimo ótimo, deve ser passado o índice da página, 
		 * ao invés do padrão que é número da página:
		 */
		if (getReplacementAlgorithm() instanceof Optimal) {
			getReplacementAlgorithm().insert(pageIndex);
		} else {
			getReplacementAlgorithm().insert(pageNumber);
		}
	}

	public ReplacementAlgorithm getReplacementAlgorithm() {
		return replacementAlgorithm;
	}
	
	public void setReplacementAlgorithm(ReplacementAlgorithm replacementAlgorithm) {
		this.replacementAlgorithm = replacementAlgorithm;
	}
}
