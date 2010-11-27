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
import javax.swing.table.DefaultTableModel;

import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;


@SuppressWarnings("serial")
public class AlgorithmPanel extends JPanel {
	
	private ReplacementAlgorithm replacementAlgorithm;
	private int currentFrameIndex = -1;
	
	private JTextField jtfReplacedPage;
	private JTextField jtfPageFaults;
	private JTextField jtfAlgorithm;
	private JTable jtbMemory;
	
	private Color defaultCellBackground;
	private Border defaultCellBorder; 

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
		JPanel jpnMemory = new JPanel();
		jtbMemory = new JTable();
		jtbMemory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jtbMemory.setRowSelectionAllowed(false);
		jtbMemory.setColumnSelectionAllowed(false);
		jtbMemory.setCellSelectionEnabled(false);
		jpnMemory.add(jtbMemory);
		
		setup(null, 0);
		
		defaultCellBackground = getJtbMemoryCellRenderer(0, 0).getBackground();
		defaultCellBorder = ((JLabel) getJtbMemoryCellRenderer(0, 0)).getBorder();
		
		return jpnMemory;
	}

	private JLabel getJtbMemoryCellRenderer(int i, int j) {
		return (JLabel) jtbMemory.getCellRenderer(i, j).getTableCellRendererComponent(jtbMemory, "", jtbMemory.isCellSelected(i, j), false, i, j);
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
			currentFrameIndex = replacementAlgorithm.insert(pageIndex);
		} else {
			currentFrameIndex = replacementAlgorithm.insert(pageNumber);
		}
		
		//Altera o conteúdo na tabela e no status:
		jtfReplacedPage.setText(jtbMemory.getValueAt(1, 1 + currentFrameIndex).toString());
		jtbMemory.setValueAt(pageNumber, 1, 1 + currentFrameIndex);
		jtfPageFaults.setText(Integer.toString(replacementAlgorithm.getPageFaultCount()));
		
		jtbMemory.repaint();
	}

	public ReplacementAlgorithm getReplacementAlgorithm() {
		return replacementAlgorithm;
	}
	
	public void setup(ReplacementAlgorithm replacementAlgorithm, int framesNumber) {
		this.replacementAlgorithm = replacementAlgorithm;		
		if (replacementAlgorithm != null) {
			jtfAlgorithm.setText(replacementAlgorithm.getName());
		}
		
		setDataModel(new int[framesNumber]);
	}

	private void setDataModel(int[] frames) {
		String[][] data = new String[2][1 + frames.length];
		
		//Create the memory table:
		data[0][0] = "Frame";
		data[1][0] = "Page";
		int index = 0;
		for (int i = 1; i < data[0].length; i++) {
			data[0][i] = Integer.toString(index);
			data[1][i] = (frames[index] == -1) ? "" : Integer.toString(frames[index]);
			index++;
		}
		
		jtbMemory.setModel(new DefaultTableModel(data, data[0]) {
			@Override
			public boolean isCellEditable(int i, int j) {
				return false;
			}
		});
		
		for (int j = 0; j < jtbMemory.getColumnModel().getColumnCount(); j++) {
			jtbMemory.getColumnModel().getColumn(j).setCellRenderer(defaultTableCellRenderer);
			jtbMemory.getColumnModel().getColumn(j).sizeWidthToFit();
		}

//		jtbMemory.repaint();
	}
	
	private DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable jtable, Object obj, boolean flag, boolean flag1, int i, int j) {
			JLabel jlbCell = (JLabel) super.getTableCellRendererComponent(jtable, obj, flag, flag1, i, j);;
			if (j == 0) {
				jlbCell.setHorizontalAlignment(JLabel.RIGHT);
				jlbCell.setFont(jlbCell.getFont().deriveFont(Font.BOLD));
			} else {				
				jlbCell.setHorizontalAlignment(JLabel.CENTER);
			}
			if (currentFrameIndex == -1) {
				return jlbCell;
			}
			if (j == 1 + currentFrameIndex) {
				jlbCell.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				jlbCell.setBackground(Color.WHITE);
			} else if (jlbCell.getBackground() != defaultCellBackground) {
				jlbCell.setBackground(defaultCellBackground);
				jlbCell.setBorder(defaultCellBorder);
			}
			return jlbCell; 
		}
	};
}
