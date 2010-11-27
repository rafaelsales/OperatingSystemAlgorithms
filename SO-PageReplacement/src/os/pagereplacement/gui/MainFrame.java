package os.pagereplacement.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import os.pagereplacement.PageGenerator;
import os.pagereplacement.algorithm.FIFO;
import os.pagereplacement.algorithm.LFU;
import os.pagereplacement.algorithm.LRU;
import os.pagereplacement.algorithm.MFU;
import os.pagereplacement.algorithm.Optimal;
import os.pagereplacement.algorithm.ReplacementAlgorithm;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {	
	
	private enum ExecutionState {
		SETUP, READY, PLAY, END
	}
	
	private static final int ALGORITHMS_COUNT = 5;
	
	private JTextField jtfReferenceStringSize;
	private JTextField jtfFramesNumber;
	private JTextField jtfPlayStepInterval;
	private JTextField jtfInsertedPage;
	private JTextField jtfInsertedPageIndex;
	
	private JToggleButton jbtSetup;
	private JButton jbtPlay;
	private JButton jbtPause;
	private JButton jbtSingleStep;
	private JButton jbtStop;
	
	private JScrollPane jspAlgorithms;
	private List<AlgorithmPanel> algorithmsPanels;
	
	private int referenceStringSize = 20; //Tamanho da cadeia de páginas
	private int framesNumber = 8; //Quantidade de frames de memória
	private int[] referenceString; //Cadeia de páginas
	private int currentPageIndex; //Índice da última página lida da cadeia de páginas
	
	private ExecutionState currentState;
	
	private Timer timer;
	private int timerSleepPeriod = 500;

	
	public MainFrame() {
		
		super("Page Replacement");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		createComponents();
		setPreferredSize(new Dimension(800, 600));
		currentState = ExecutionState.SETUP;
		jbtSetup.setSelected(true);
		
		enableDisableControls();
		
		pack();
		setVisible(true);
	}
	
	private void createComponents() {
		JPanel jpnMainPanel = (JPanel) getContentPane();
		jpnMainPanel.setBorder(new EmptyBorder(new Insets(4, 6, 4, 6)));
		jpnMainPanel.setLayout(new BorderLayout());
		
		jspAlgorithms = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(createControlPanel(), BorderLayout.NORTH);
		add(jspAlgorithms, BorderLayout.CENTER);
		add(createStatusPanel(), BorderLayout.SOUTH);
	}

	private Component createStatusPanel() {
		JLabel jlbInsertedPage = new JLabel("Inserted page:");
		JLabel jlbInsertedPageIndex = new JLabel("Inserted page index:");
		
		jtfInsertedPage = new JTextField(5);
		jtfInsertedPage.setEditable(false);
		
		jtfInsertedPageIndex = new JTextField(5);
		jtfInsertedPageIndex.setEditable(false);
		
		JPanel jpnStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
		jpnStatus.setBorder(new TitledBorder("Status"));
		
		jpnStatus.add(jlbInsertedPage);
		jpnStatus.add(jtfInsertedPage);
		jpnStatus.add(new JSeparator(SwingConstants.VERTICAL));
		jpnStatus.add(jlbInsertedPageIndex);
		jpnStatus.add(jtfInsertedPageIndex);
		
		return jpnStatus;
	}

	private JPanel createControlPanel() {
		JLabel jlbReferenceStringSize = new JLabel("Reference String Size:");
		JLabel jlbNumberFrame = new JLabel("Frame count:");
		JLabel jlbPlayStepInterval = new JLabel("Play step interval (ms):");
		
		jtfReferenceStringSize = new JTextField(Integer.toString(referenceStringSize), 15);
		jtfFramesNumber = new JTextField(Integer.toString(framesNumber), 15);
		jtfPlayStepInterval = new JTextField(Integer.toString(timerSleepPeriod), 15);
		
		jbtSetup = new JToggleButton("Setup");
		jbtSetup.addActionListener(buttonsActionListener);
		
		jbtPlay = new JButton("Play");
		jbtPlay.addActionListener(buttonsActionListener);
		
		jbtPause = new JButton("Pause");
		jbtPause.addActionListener(buttonsActionListener);

		jbtSingleStep = new JButton("1 Step");
		jbtSingleStep.addActionListener(buttonsActionListener);
		
		jbtStop = new JButton("Stop");
		jbtStop.addActionListener(buttonsActionListener);

		//Cria o panel dos campos:
		JPanel jpnFields = new JPanel(new GridLayout(2, 3, 4, 2));
		jpnFields.add(jlbReferenceStringSize);
		jpnFields.add(jlbNumberFrame);
		jpnFields.add(jlbPlayStepInterval);
		jpnFields.add(jtfReferenceStringSize);
		jpnFields.add(jtfFramesNumber);
		jpnFields.add(jtfPlayStepInterval);
		
		//Cria o panel dos botões:
		JPanel jpnButtons = new JPanel(new GridLayout(1, 4, 4, 2));
		jpnButtons.add(jbtSetup);
		jpnButtons.add(jbtPlay);
		jpnButtons.add(jbtPause);
		jpnButtons.add(jbtSingleStep);
		jpnButtons.add(jbtStop);
		
		//Cria o panel do painel de controle que inclui o panel dos campos e dos botões:
		JPanel jpnControlPanel = new JPanel(new BorderLayout());
		jpnControlPanel.setBorder(new TitledBorder("Control Panel"));
		jpnControlPanel.add(jpnFields, BorderLayout.NORTH);
		jpnControlPanel.add(jpnButtons, BorderLayout.SOUTH);
		
		return jpnControlPanel;
	}
	
	private void prepareSetup() {
		renderStatus(null, null);
		currentState = ExecutionState.SETUP;
	}
	
	private void setup() {		
		try {
			referenceStringSize = Integer.parseInt(jtfReferenceStringSize.getText());
			if (referenceStringSize <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			showErrorDialog("Enter a valid reference string size", true);
			return;
		}
		try {
			framesNumber = Integer.parseInt(jtfFramesNumber.getText());
			if (framesNumber <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			showErrorDialog("Enter a valid number of frames", true);
			return;
		}
		try {
			timerSleepPeriod = Integer.parseInt(jtfPlayStepInterval.getText());
			if (timerSleepPeriod <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			showErrorDialog("Enter a valid play step interval", true);
			return;
		}
		
		//Gera as páginas:
		PageGenerator pageGenerator = new PageGenerator(referenceStringSize);
		referenceString = pageGenerator.getReferenceString();

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
		pack();
		currentState = ExecutionState.READY;
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
	}
	
	private synchronized void stop() {
		if (timer != null) {
			timer.stop();
		}

		//Reinicia para a primeira página:
		currentPageIndex = -1;
		renderStatus(null, null);
		
		//Recria os algoritmos e os define em seus respectivos painéis:
		List<ReplacementAlgorithm> algorithms = createAlgorihtms();
		for (int i = 0; i < algorithms.size(); i++) {
			algorithmsPanels.get(i).setup(algorithms.get(i), framesNumber);
		}		
		
		currentState = ExecutionState.READY;
	}
	
	private void showErrorDialog(String message, boolean error) {
		int messageType = error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		JOptionPane.showMessageDialog(this, message, getTitle(), messageType);
	}
	
	private void enableDisableControls() {
		boolean canSetup = (currentState == ExecutionState.SETUP || currentState == ExecutionState.READY || currentState == ExecutionState.END);
		boolean canPlay = (currentState == ExecutionState.READY);
		boolean canPause = (currentState == ExecutionState.PLAY);
		boolean canDoSingleStep = (currentState == ExecutionState.READY);
		boolean canStop = (currentState == ExecutionState.PLAY || currentState == ExecutionState.READY || currentState == ExecutionState.END);
		
		jbtSetup.setEnabled(canSetup);
		jbtPlay.setEnabled(canPlay);
		jbtPause.setEnabled(canPause);
		jbtSingleStep.setEnabled(canDoSingleStep);
		jbtStop.setEnabled(canStop);
		
		boolean canEditFields = jbtSetup.isSelected();
		jtfFramesNumber.setEnabled(canEditFields);
		jtfReferenceStringSize.setEnabled(canEditFields);
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

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new MainFrame();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}