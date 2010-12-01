package os.pagereplacement.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class TextAreaDialog extends JDialog {
	
	public TextAreaDialog(String label, String text, JPanel parentPanel) {
		super((Frame) null, MainPanel.APPLICATION_TITLE, true);
		setLayout(new BorderLayout(4, 4));
		((JPanel) getContentPane()).setBorder(new EmptyBorder(6, 6, 6, 6));
		
		JTextArea jtaContent = new JTextArea(text);
		jtaContent.setLineWrap(true);
		jtaContent.setWrapStyleWord(true);
		jtaContent.setEditable(false);
		
		JButton jbtClose = new JButton("Close");
		jbtClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				TextAreaDialog.this.dispose();
			}
		});
		
		JPanel jpnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jpnButtons.add(jbtClose);

		add(new JLabel(label), BorderLayout.NORTH);
		add(new JScrollPane(jtaContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.CENTER);
		add(jpnButtons, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(360, 240));
		pack();
		setLocation((int) parentPanel.getBounds().getCenterX() - getWidth() / 2, (int) parentPanel.getBounds().getCenterY() - getHeight() / 2);
		jbtClose.requestFocus();
		setVisible(true);
	}
}
