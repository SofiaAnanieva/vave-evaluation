package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.i18n.Translator;


class ProblemsDialog extends JDialog implements ActionListener {
	private Frame parentFrame;
	private JButton abortButton;
	private JButton continueButton;
	private JLabel northLabel;
	private boolean aborted = false;
	ProblemsDialog(Frame frame,String errors) {
			super(frame);
			setResizable(true);
			setModal(true);
			setTitle(Translator.localize("dialog.title.import-problems"));
			Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
			getContentPane().setLayout(new BorderLayout(0,0));
			northLabel = new JLabel(Translator.localize("label.import-problems"));
			getContentPane().add(northLabel,BorderLayout.NORTH);
			JEditorPane textArea = new JEditorPane();
			textArea.setText(errors);
			JPanel centerPanel = new JPanel(new BorderLayout());
			centerPanel.add(new JScrollPane(textArea));
			centerPanel.setPreferredSize(new Dimension(600,200));
			getContentPane().add(centerPanel);
			continueButton = new JButton(Translator.localize("button.continue"));
			abortButton = new JButton(Translator.localize("button.abort"));
			JPanel bottomPanel = new JPanel();
			bottomPanel.add(continueButton);
			bottomPanel.add(abortButton);
			getContentPane().add(bottomPanel,BorderLayout.SOUTH);
			continueButton.requestFocusInWindow();
			continueButton.addActionListener(this);
			abortButton.addActionListener(this);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					disposeDialog();
				}
			});
			pack();
			Dimension contentPaneSize = getContentPane().getSize();
			setLocation(scrSize.width / 2 - contentPaneSize.width / 2,scrSize.height / 2 - contentPaneSize.height / 2);
		}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abortButton)) {
			aborted = true;
		}
		disposeDialog();
	}
	public boolean isAborted() {
		return aborted;
	}
	private void disposeDialog() {
		setVisible(false);
		dispose();
	}
	private static final long serialVersionUID = -9221358976863603143l;
}



