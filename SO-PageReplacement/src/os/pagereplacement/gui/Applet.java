package os.pagereplacement.gui;

import javax.swing.JApplet;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Applet extends JApplet {
	
	public Applet() {
		try {
			// Define o Look And Feel para o padrão do Sistema Operacional
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
	}

	@Override
	public void init() {
		super.init();
		final int referenceStringSize = 500;
		final int frameSize = 100;
		setContentPane(new MainPanel(referenceStringSize, frameSize));
		doLayout();
	}
}
