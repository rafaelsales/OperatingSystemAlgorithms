package os.pagereplacement.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import os.pagereplacement.PageGenerator;

public class MainFrame extends JFrame {	
	
	private JTextField jtfReferenceStringSize;
	private JTextField jtfbFramesCount;
	
	private JButton jbtGenerateReferenceString;
	private JButton jbtPlay;
	private JButton jbtPause;
	private JButton jbtSingleStep;
	private JButton jbtStop;
	
	public MainFrame() {
		super("Page Replacement");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		createComponents();
		
//		setSize(640, 480);
		pack();
		setVisible(true);
	}
	
	private void createComponents() {
		JPanel jpnMainPanel = (JPanel) getContentPane();
		jpnMainPanel.setBorder(new EmptyBorder(new Insets(4, 6, 4, 6)));
		jpnMainPanel.setLayout(new BorderLayout());
		
		jpnMainPanel.add(createControlPanel(), BorderLayout.NORTH);
		jpnMainPanel.add(createAlgorithmsPanel(), BorderLayout.SOUTH);
	}

	private JPanel createControlPanel() {
		JLabel jlbReferenceStringSize = new JLabel("Reference String Size:");
		JLabel jlbFrameCount = new JLabel("Frame count:");;
		
		jtfReferenceStringSize = new JTextField("500", 15);
		jtfbFramesCount = new JTextField("100", 15);
		
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

		//Cria o panel dos campos:
		JPanel jpnFields = new JPanel(new GridLayout(2, 2, 4, 2));
		jpnFields.add(jlbReferenceStringSize);
		jpnFields.add(jlbFrameCount);
		jpnFields.add(jtfReferenceStringSize);
		jpnFields.add(jtfbFramesCount);
		
		//Cria o panel dos botões:
		JPanel jpnButtons = new JPanel(new GridLayout(1, 4, 4, 2));
		jpnButtons.add(jbtPlay);
		jpnButtons.add(jbtPause);
		jpnButtons.add(jbtSingleStep);
		jpnButtons.add(jbtStop);
		
		//Cria o panel do painel de controle que inclui o panel dos campos e dos botões:
		JPanel jpnControlPanel = new JPanel(new BorderLayout());
		jpnControlPanel.add(jpnFields, BorderLayout.NORTH);
		jpnControlPanel.add(jpnButtons, BorderLayout.SOUTH);
		
		return jpnControlPanel;
	}
	
	private JPanel createAlgorithmsPanel() {
		JPanel jpnAlgorithmsPanel = new JPanel();
		
		return jpnAlgorithmsPanel;
	}
	
	private void play() {
//		PageGenerator pageGenerator = new PageGenerator(new Integer(args[0]).intValue());
	}
	
	private void pause() {
		
	}
	
	private void doSingleStep() {
		
	}
	
	private void stop() {

	}

	private void initPageReplacement() {
//		PageGenerator pageGenerator = new PageGenerator(new Integer(args[0]).intValue());
//
//		int[] referenceString = pageGenerator.getReferenceString();
//
//		List<ReplacementAlgorithm> replacementAlgorithms = new ArrayList<ReplacementAlgorithm>();
//		/** Use either the FIFO or LRU algorithms */
//		ReplacementAlgorithm fifo = new FIFO(new Integer(args[1]).intValue());
//		ReplacementAlgorithm lru = new LRU(new Integer(args[1]).intValue());
//		ReplacementAlgorithm lfu = new LFU(new Integer(args[1]).intValue());
//		ReplacementAlgorithm mfu = new MFU(new Integer(args[1]).intValue());
//		ReplacementAlgorithm optimal = new Optimal(new Integer(args[1]).intValue(), referenceString);
//		replacementAlgorithms.add(fifo);
//		replacementAlgorithms.add(lru);
//		replacementAlgorithms.add(lfu);
//		replacementAlgorithms.add(mfu);
//		replacementAlgorithms.add(optimal);
//
//		for (int i = 0; i < referenceString.length; i++) {
//			for (ReplacementAlgorithm replacementAlgorithm : replacementAlgorithms) {
//				if (replacementAlgorithm instanceof Optimal) {
//					replacementAlgorithm.insert(i);
//				} else {
//					replacementAlgorithm.insert(referenceString[i]);
//				}
//			}
//		}
//
//		// report the total number of page faults
//		System.out.println("LRU faults = " + lru.getPageFaultCount());
//		System.out.println("FIFO faults = " + fifo.getPageFaultCount());
//		System.out.println("LFU faults = " + lfu.getPageFaultCount());
//		System.out.println("MFU faults = " + mfu.getPageFaultCount());
//		System.out.println("OPTIMAL faults = " + optimal.getPageFaultCount());
	}
	
	private ActionListener buttonsActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (jbtGenerateReferenceString == e.getSource()) {
				
			} else if (jbtPlay == e.getSource()) {
				
			} else if (jbtPause == e.getSource()) {
				
			} else if (jbtSingleStep == e.getSource()) {
				
			} else if (jbtStop == e.getSource()) {
			}
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
