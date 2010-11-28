package os.pagereplacement.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;


@SuppressWarnings("serial")
public class AlgorithmPanel extends JPanel {
	
	private ReplacementAlgorithm replacementAlgorithm;
	private int currentFrameIndex = -1;
	private boolean pageAlreadyInMemory = false;
	
	private JTextField jtfReplacedPage;
	private JTextField jtfPageFaults;
	private JTextField jtfAlgorithm;
	private JTable jtbMemory;

	public AlgorithmPanel() {
		setLayout(new BorderLayout());
		
		add(createStatusPanel(), BorderLayout.NORTH);
		add(createMemoryPanel(), BorderLayout.CENTER);
	}

	private JPanel createStatusPanel() {
		JLabel jlbReplacedPage = new JLabel("Last Replaced page:");
		jtfReplacedPage = new JTextField(4);
		jtfReplacedPage.setEditable(false);
		
		JLabel jlbPageFaults = new JLabel("Page faults:");
		jtfPageFaults = new JTextField(4);
		jtfPageFaults.setEditable(false);
		
		JLabel jlbAlgorihtm = new JLabel("Algorithm:");
		jtfAlgorithm = new JTextField();
		jtfAlgorithm.setEditable(false);
		
		JPanel jpnStatus = new JPanel(new FlowLayout(FlowLayout.LEADING, 2, 2));
		jpnStatus.add(jlbAlgorihtm);
		jpnStatus.add(jtfAlgorithm);
		jpnStatus.add(new JSeparator(SwingConstants.VERTICAL));
		jpnStatus.add(jlbReplacedPage);
		jpnStatus.add(jtfReplacedPage);
		jpnStatus.add(new JSeparator(SwingConstants.VERTICAL));
		jpnStatus.add(jlbPageFaults);
		jpnStatus.add(jtfPageFaults);
		
		
		return jpnStatus;
	}
	
	private JPanel createMemoryPanel() {
		JPanel jpnMemory = new JPanel(new BorderLayout());
		jtbMemory = new JTable();
		jtbMemory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jtbMemory.setRowSelectionAllowed(false);
		jtbMemory.setColumnSelectionAllowed(false);
		jtbMemory.setCellSelectionEnabled(false);
		jtbMemory.setTableHeader(null);
		jpnMemory.add(jtbMemory, BorderLayout.CENTER);
		
		return jpnMemory;
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
		pageAlreadyInMemory = replacementAlgorithm.getPageFrameIndex(pageNumber) != -1;
		if (getReplacementAlgorithm() instanceof Optimal) {
			currentFrameIndex = replacementAlgorithm.insert(pageIndex);
		} else {
			currentFrameIndex = replacementAlgorithm.insert(pageNumber);
		}
		
		if (!pageAlreadyInMemory) {
			//Altera o conteúdo na tabela e no status:
			jtfReplacedPage.setText(jtbMemory.getValueAt(1, 1 + currentFrameIndex).toString());
		}
		jtbMemory.setValueAt(pageNumber, 1, 1 + currentFrameIndex);
		jtfPageFaults.setText(Integer.toString(replacementAlgorithm.getPageFaultCount()));
		
		jtbMemory.repaint();
	}

	public ReplacementAlgorithm getReplacementAlgorithm() {
		return replacementAlgorithm;
	}
	
	public void setup(ReplacementAlgorithm replacementAlgorithm, int framesNumber) {
		currentFrameIndex = -1;
		pageAlreadyInMemory = false;
		this.replacementAlgorithm = replacementAlgorithm;
		jtfPageFaults.setText("");
		jtfReplacedPage.setText("");
		if (replacementAlgorithm != null) {
			jtfAlgorithm.setText(replacementAlgorithm.getName());
		}
		
		setJtbMemoryDataModel(replacementAlgorithm.getFrames());
	}

	private void setJtbMemoryDataModel(int[] frames) {
		String[][] data = new String[2][1 + frames.length];
		
		//Create the memory table:
		data[0][0] = "Frame No.";
		data[1][0] = "Page No.";
		int index = 0;
		for (int i = 1; i < data[0].length; i++) {
			data[0][i] = Integer.toString(index);
			data[1][i] = (frames[index] == -1) ? "" : Integer.toString(frames[index]);
			index++;
		}
		
		SwingUtil.setupTable(jtbMemory, defaultTableCellRenderer, data);
	}
	
	private DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() {
		private Color defaultCellBackground;
		private Border defaultCellBorder; 
		
		@Override
		public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j) {
			JLabel jlbCell = (JLabel) super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);;
			if (defaultCellBackground == null) {
				defaultCellBackground = jlbCell.getBackground();
				defaultCellBorder = jlbCell.getBorder();
			}
			if (j == 0) {
				//Se for a primeira coluna, define o alinhamento e fonte negrito:
				jlbCell.setHorizontalAlignment(JLabel.RIGHT);
				jlbCell.setFont(jlbCell.getFont().deriveFont(Font.BOLD));
			} else {
				//As demais colunas tem o conteúdo alinhadas no centro: 
				jlbCell.setHorizontalAlignment(JLabel.CENTER);
			}
			
			//Destaca o frame atual:
			if (currentFrameIndex != -1 && j == 1 + currentFrameIndex) {
				if (pageAlreadyInMemory) {
					jlbCell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
				} else {
					jlbCell.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				}
				jlbCell.setBackground(Color.WHITE);
			} else if (jlbCell.getBackground() != defaultCellBackground) {
				jlbCell.setBackground(defaultCellBackground);
				jlbCell.setBorder(defaultCellBorder);
			}
			return jlbCell;
		}
	};
}
