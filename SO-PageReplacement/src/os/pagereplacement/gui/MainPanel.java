package os.pagereplacement.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import os.pagereplacement.PageGenerator;
import os.pagereplacement.algorithm.FIFO;
import os.pagereplacement.algorithm.LFU;
import os.pagereplacement.algorithm.LRU;
import os.pagereplacement.algorithm.MFU;
import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	private enum ExecutionState {
		SETUP, READY, PLAY, END
	}
	
	public static final String APPLICATION_TITLE = "Page Replacement";
	private static final int ALGORITHMS_COUNT = 5;
	
	private JTextField jtfNumberOfFrames;
	private JTextField jtfPlayStepInterval;
	private JTextField jtfInsertedPage;
	private JTextField jtfInsertedPageIndex;
	
	private JToggleButton jbtSetup;
	private JButton jbtDefineReferenceString;
	private JButton jbtGenerateReferenceString;
	private JButton jbtPlay;
	private JButton jbtPause;
	private JButton jbtSingleStep;
	private JButton jbtStop;
	
	private JScrollPane jspAlgorithms;
	private List<AlgorithmPanel> algorithmsPanels;
	private JTable jtbReferenceString;
	private JScrollPane jspReferenceString;
	
	private final int DEFAULT_REFERENCE_STRING_SIZE; //Tamanho padrão para a cadeia de referencias de páginas
	private int framesNumber; //Quantidade de frames de memória
	private int[] referenceString; //Cadeia de páginas
	private int currentPageIndex = -1; //Índice da última página lida da cadeia de páginas
	
	private ExecutionState currentState;
	
	private Timer timer;
	private int timerSleepPeriod = 0;

	public MainPanel(int referenceStringSize, int framesNumber) {
		this.DEFAULT_REFERENCE_STRING_SIZE = referenceStringSize;
		this.framesNumber = framesNumber;
		
		createComponents();
		
		setPreferredSize(new Dimension(800, 650));
		
		currentState = ExecutionState.SETUP;
		jbtSetup.setSelected(true);
		enableDisableControls();
	}
	
	private void createComponents() {
		setBorder(new EmptyBorder(new Insets(4, 6, 4, 6)));
		setLayout(new BorderLayout());
		
		jspAlgorithms = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		add(createControlPanel(), BorderLayout.NORTH);
		add(jspAlgorithms, BorderLayout.CENTER);
		add(createStatusPanel(), BorderLayout.SOUTH);
	}

	private JPanel createControlPanel() {
		JLabel jlbNumberOfFrames = new JLabel("Number of frames:");
		JLabel jlbPlayStepInterval = new JLabel("Play step interval (ms):");
		
		{
			jtfNumberOfFrames = new JTextField(Integer.toString(framesNumber), 15);
			jtfPlayStepInterval = new JTextField(Integer.toString(timerSleepPeriod), 15);
		}
		
		{
			jbtSetup = new JToggleButton("Setup");
			jbtSetup.addActionListener(buttonsActionListener);
			
			jbtDefineReferenceString = new JButton("Set Reference String");
			jbtDefineReferenceString.addActionListener(buttonsActionListener);
			
			jbtGenerateReferenceString = new JButton("Generate Reference String");
			jbtGenerateReferenceString.addActionListener(buttonsActionListener);
			
			jbtPlay = new JButton("Play");
			jbtPlay.addActionListener(buttonsActionListener);
			
			jbtPause = new JButton("Pause");
			jbtPause.addActionListener(buttonsActionListener);
	
			jbtSingleStep = new JButton("1 Step");
			jbtSingleStep.addActionListener(buttonsActionListener);
			
			jbtStop = new JButton("Stop");
			jbtStop.addActionListener(buttonsActionListener);
		}

		//Cria o panel dos campos:
		JPanel jpnFields = new JPanel(new GridLayout(2, 2, 4, 2));
		{
			jpnFields.setBorder(new EmptyBorder(0, 4, 0, 4));
			jpnFields.add(jlbNumberOfFrames);
			jpnFields.add(jlbPlayStepInterval);
			jpnFields.add(jtfNumberOfFrames);
			jpnFields.add(jtfPlayStepInterval);
		}
		
		//Cria o panel dos botões:
		JPanel jpnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
		{
			jpnButtons.add(jbtSetup);
			jpnButtons.add(jbtDefineReferenceString);
			jpnButtons.add(jbtGenerateReferenceString);
			jpnButtons.add(jbtPlay);
			jpnButtons.add(jbtPause);
			jpnButtons.add(jbtSingleStep);
			jpnButtons.add(jbtStop);
		}
		
		//Cria o panel do painel de controle que inclui o panel dos campos e dos botões:
		JPanel jpnControlPanel = new JPanel(new BorderLayout(2, 2));
		{
			jpnControlPanel.setBorder(new TitledBorder("Control Panel"));
			jpnControlPanel.add(jpnFields, BorderLayout.NORTH);
			jpnControlPanel.add(jpnButtons, BorderLayout.SOUTH);
		}
		
		return jpnControlPanel;
	}
	

	private Component createStatusPanel() {
		JLabel jlbInsertedPage = new JLabel("Inserted page:");
		JLabel jlbInsertedPageIndex = new JLabel("Inserted page index:");
		
		jtfInsertedPage = new JTextField(5);
		jtfInsertedPage.setEditable(false);
		
		jtfInsertedPageIndex = new JTextField(5);
		jtfInsertedPageIndex.setEditable(false);
		
		//Criação dos campos de status:
		JPanel jpnStatusFields = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
		{
			jpnStatusFields.setBorder(new TitledBorder("Status"));
			jpnStatusFields.add(jlbInsertedPage);
			jpnStatusFields.add(jtfInsertedPage);
			jpnStatusFields.add(new JSeparator(SwingConstants.VERTICAL));
			jpnStatusFields.add(jlbInsertedPageIndex);
			jpnStatusFields.add(jtfInsertedPageIndex);	
		}
		
		//Criação da tabela para exibição da cadeia de referencia de páginas	
		JPanel jpnReferenceStringTable = new JPanel(new BorderLayout());
		{
			jtbReferenceString = new JTable();
			jtbReferenceString.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jtbReferenceString.setRowSelectionAllowed(false);
			jtbReferenceString.setColumnSelectionAllowed(false);
			jtbReferenceString.setCellSelectionEnabled(false);
			jtbReferenceString.setTableHeader(null);
			setJtbReferenceStringDataModel(null);
			
			jpnReferenceStringTable.add(jtbReferenceString, BorderLayout.CENTER);
			
			jspReferenceString = new JScrollPane(jpnReferenceStringTable, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jspReferenceString.setMinimumSize(jtbReferenceString.getPreferredScrollableViewportSize());
			jspReferenceString.setBorder(new TitledBorder("Reference String"));
		}
		
		JPanel jpnStatus = new JPanel(new BorderLayout());
		{
			jpnStatus.add(jspReferenceString, BorderLayout.NORTH);
			jpnStatus.add(jpnStatusFields, BorderLayout.CENTER);
		}
		
		return jpnStatus;
	}
	
	private void setJtbReferenceStringDataModel(int[] referenceString) {
		if (referenceString == null) {
			referenceString = new int[0];
		}
		String[][] data = new String[1][1 + referenceString.length];
		
		//Create the memory table:;
		data[0][0] = "Page No.";
		int index = 0;
		for (int i = 1; i < data[0].length; i++) {
			data[0][i] = Integer.toString(referenceString[index]);
			index++;
		}
		SwingUtil.setupTable(jtbReferenceString, defaultTableCellRenderer, data);
	}
	
	private void prepareSetup() {
		renderStatus(null, null);
		setJtbReferenceStringDataModel(null);
		currentState = ExecutionState.SETUP;
	}
	
	private void setup() {
		try {
			framesNumber = Integer.parseInt(jtfNumberOfFrames.getText());
			if (framesNumber <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			showMessageDialog("Enter a valid number of frames!", true);
			return;
		}
		try {
			timerSleepPeriod = Integer.parseInt(jtfPlayStepInterval.getText());
			if (timerSleepPeriod < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			showMessageDialog("Enter a valid play step interval!", true);
			return;
		}
		
		if (referenceString == null) {
			generateReferenceString();
		} else {
			new TextAreaDialog("Using the following reference string:", Arrays.toString(referenceString), this);
		}

		//Cria os painéis para exibição dos estados dos algorítmos:
		JPanel jpnAlgorithmsPanel = new JPanel(new GridLayout(ALGORITHMS_COUNT, 1));
		algorithmsPanels = new ArrayList<AlgorithmPanel>(ALGORITHMS_COUNT);
		for (int i = 0; i < ALGORITHMS_COUNT; i++) {
			AlgorithmPanel algorithmPanel = new AlgorithmPanel();
			algorithmsPanels.add(algorithmPanel);
			
			//Adiciona o painel à tela:
			jpnAlgorithmsPanel.add(algorithmPanel);
		}
		
		jspAlgorithms.setViewportView(jpnAlgorithmsPanel);
		
		stop();
		currentState = ExecutionState.READY;
	}

	private void defineReferenceString() {
		try {
			String referenceStr = JOptionPane.showInputDialog(this, "Enter a comma separated reference string. Example: 30, 51, 25");
			if (referenceStr == null) {
				return;
			}
			referenceStr = referenceStr.replaceAll("[ {}\\[\\]]", "");
			String[] referenceStrArray = referenceStr.split(",");
			referenceString = new int[referenceStrArray.length];
			for (int i = 0; i < referenceStrArray.length; i++) {
				referenceString[i] = Integer.parseInt(referenceStrArray[i]);
			}
			setJtbReferenceStringDataModel(referenceString);
		} catch (Exception e) {
			referenceString = null;
			showMessageDialog("Invalid reference string!", true);
		}
	}

	private void generateReferenceString() {
		int referenceStringSize;
		int pageNumberRange;
		do {
			try {
				String referenceStringSizeStr = JOptionPane.showInputDialog(this, "Enter the reference string size:", DEFAULT_REFERENCE_STRING_SIZE);
				if (referenceStringSizeStr == null) {
					return;
				} else {
					referenceStringSize = Integer.parseInt(referenceStringSizeStr);
					if (referenceStringSize <= 0) {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				showMessageDialog("Enter a valid reference string size!", true);
				return;
			}
		} while (referenceStringSize <= 0);
		do {
			try {
				String pageNumberRangeStr = JOptionPane.showInputDialog(this, "Enter the page number range:", PageGenerator.RANGE);
				if (pageNumberRangeStr == null) {
					return;
				} else {
					pageNumberRange = Integer.parseInt(pageNumberRangeStr);
					if (pageNumberRange <= 0) {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				showMessageDialog("Enter a valid page number range!", true);
				return;
			}
		} while (referenceStringSize <= 0);
		
		//Gera as páginas:
		PageGenerator pageGenerator = new PageGenerator(referenceStringSize, pageNumberRange);
		referenceString = pageGenerator.getReferenceString();
		new TextAreaDialog("The following reference string has been generated:", Arrays.toString(referenceString), this);
	}
	
	private synchronized void play() {
		ActionListener actionlistener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				doSingleStep();				
			}
		};
		currentState = ExecutionState.PLAY;
		// Inicia a tarefa de execução, que é executada em intervalos de 'timerSleepPeriod' milisegundos:
		timer = new Timer(timerSleepPeriod, actionlistener);
		timer.start();
	}
	
	private synchronized void pause() {
		timer.stop();
		
		currentState = ExecutionState.READY;
	}
	
	private synchronized void doSingleStep() {
		currentPageIndex++;
		for (AlgorithmPanel algorithmPanel : algorithmsPanels) {
			algorithmPanel.insert(referenceString[currentPageIndex], currentPageIndex);
		}
		renderStatus(referenceString[currentPageIndex], currentPageIndex);
		
		if (currentPageIndex == referenceString.length - 1) {
			currentState = ExecutionState.END;
			if (timer != null && timer.isRunning()) {
				timer.stop();
			}
			enableDisableControls();
		}
		
		scrollJtbReferenceString();
	}

	private void scrollJtbReferenceString() {
		Rectangle cellRect = jtbReferenceString.getCellRect(0, currentPageIndex + 1, true);
		cellRect.x -= cellRect.width;
		cellRect.width *= 2;
		jtbReferenceString.scrollRectToVisible(cellRect);
		jtbReferenceString.repaint();
	}
	
	private synchronized void stop() {
		if (timer != null) {
			timer.stop();
		}

		//Reinicia para a primeira página:
		currentPageIndex = -1;
		renderStatus(null, null);
		setJtbReferenceStringDataModel(referenceString);
		scrollJtbReferenceString();
		
		//Recria os algoritmos e os define em seus respectivos painéis:
		List<ReplacementAlgorithm> algorithms = createAlgorihtms();
		for (int i = 0; i < algorithms.size(); i++) {
			algorithmsPanels.get(i).setup(algorithms.get(i), framesNumber);
		}		
		
		currentState = ExecutionState.READY;
	}
	
	private void showMessageDialog(String message, boolean error) {
		int messageType = error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(this, message, APPLICATION_TITLE, messageType);
	}
	
	private void enableDisableControls() {
		boolean canSetup = (currentState == ExecutionState.SETUP || currentState == ExecutionState.READY || currentState == ExecutionState.END);
		boolean canDefineReferenceString = (currentState == ExecutionState.SETUP);
		boolean canPlay = (currentState == ExecutionState.READY);
		boolean canPause = (currentState == ExecutionState.PLAY);
		boolean canDoSingleStep = (currentState == ExecutionState.READY);
		boolean canStop = (currentState == ExecutionState.PLAY || currentState == ExecutionState.READY || currentState == ExecutionState.END);
		
		jbtSetup.setEnabled(canSetup);
		jbtDefineReferenceString.setEnabled(canDefineReferenceString);
		jbtGenerateReferenceString.setEnabled(canDefineReferenceString);
		jbtPlay.setEnabled(canPlay);
		jbtPause.setEnabled(canPause);
		jbtSingleStep.setEnabled(canDoSingleStep);
		jbtStop.setEnabled(canStop);
		
		boolean canEditFields = jbtSetup.isSelected();
		
		jtfNumberOfFrames.setEnabled(canEditFields);
		jtfPlayStepInterval.setEnabled(canEditFields);
	}
	
	private void renderStatus(Integer pageNumber, Integer currentPageIndex) {
		String pageNumberStr = "";
		String currentPageIndexStr = "";
		if (pageNumber != null) {
			pageNumberStr = pageNumber.toString();
		}
		if (currentPageIndex != null) {
			currentPageIndexStr = currentPageIndex.toString();
		}
		jtfInsertedPage.setText(pageNumberStr);
		jtfInsertedPageIndex.setText(currentPageIndexStr);
	}
	
	private List<ReplacementAlgorithm> createAlgorihtms() {
		List<ReplacementAlgorithm> algorithms = new ArrayList<ReplacementAlgorithm>();
		algorithms.add(new FIFO(framesNumber));
		algorithms.add(new LRU(framesNumber));
		algorithms.add(new LFU(framesNumber));
		algorithms.add(new MFU(framesNumber));
		algorithms.add(new Optimal(framesNumber, referenceString));
		return algorithms;
	}
	
	private ActionListener buttonsActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (jbtSetup == e.getSource()) {
				if (jbtSetup.isSelected()) {
					prepareSetup();
				} else {
					setup();
				}
			} else if (jbtDefineReferenceString == e.getSource()) {
				defineReferenceString();
			} else if (jbtGenerateReferenceString == e.getSource()) {
				generateReferenceString();
			} else if (jbtPlay == e.getSource()) {
				play();
			} else if (jbtPause == e.getSource()) {
				pause();
			} else if (jbtSingleStep == e.getSource()) {
				doSingleStep();
			} else if (jbtStop == e.getSource()) {
				stop();
			}
			enableDisableControls();
		}
	};
	
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
			
			//Destaca a página atual:
			if (currentPageIndex != -1 && j == 1 + currentPageIndex) {
				jlbCell.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
				jlbCell.setBackground(Color.WHITE);
			} else if (jlbCell.getBackground() != defaultCellBackground) {
				jlbCell.setBackground(defaultCellBackground);
				jlbCell.setBorder(defaultCellBorder);
			}
			return jlbCell;
		}
	};
}