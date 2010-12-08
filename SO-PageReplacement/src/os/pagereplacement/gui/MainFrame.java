package os.pagereplacement.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import os.pagereplacement.PageGenerator;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static void main(String[] args) {
		final int referenceStringSizeFinal = PageGenerator.DEFAULT_SIZE;
		final int frameSizeFinal = 100;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new MainFrame(referenceStringSizeFinal, frameSizeFinal);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MainFrame(int referenceStringSize, int frameSize) {
		super(MainPanel.APPLICATION_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new MainPanel(referenceStringSize, frameSize));
		pack();
		setVisible(true);
	}
}
