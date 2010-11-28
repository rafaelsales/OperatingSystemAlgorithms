package os.pagereplacement.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static void main(String[] args) {
		int referenceStringSize;
		int frameSize;
		try {
			referenceStringSize = Integer.parseInt(args[0]);
			frameSize = Integer.parseInt(args[1]);
		} catch (Exception e) {
			referenceStringSize = 1000;
			frameSize = 100;
			System.out.println("Parâmetros inválidos. Utilizando parâmetros padrões: " + referenceStringSize + " " + frameSize);
		}
		
		final int referenceStringSizeFinal = referenceStringSize;
		final int frameSizeFinal = frameSize;
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
