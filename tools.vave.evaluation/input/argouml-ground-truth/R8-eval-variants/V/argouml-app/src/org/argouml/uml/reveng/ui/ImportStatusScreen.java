package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressEvent;
import org.argouml.taskmgmt.ProgressMonitor;


public class ImportStatusScreen extends JDialog implements ProgressMonitor,WindowListener {
	private JButton cancelButton;
	private JLabel progressLabel;
	private JProgressBar progress;
	private JTextArea messageArea;
	private boolean hasMessages = false;
	private boolean canceled = false;
	public ImportStatusScreen(Frame frame,String title,String iconName) {
		super(frame,true);
		if (title != null) {
			setTitle(title);
		}
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		getContentPane().setLayout(new BorderLayout(4,4));
		Container panel = new JPanel(new GridBagLayout());
		progressLabel = new JLabel();
		progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		panel.add(progressLabel,gbc);
		gbc.gridy++;
		progress = new JProgressBar();
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(progress,gbc);
		gbc.gridy++;
		panel.add(new JLabel(Translator.localize("label.import-messages")),gbc);
		gbc.gridy++;
		messageArea = new JTextArea(10,50);
		gbc.weighty = 0.8;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(messageArea),gbc);
		gbc.gridy++;
		cancelButton = new JButton(Translator.localize("button.cancel"));
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.weighty = 0.1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		panel.add(cancelButton,gbc);
		gbc.gridy++;
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isComplete()) {
					close();
				}
				canceled = true;
			}
		});
		getContentPane().add(panel);
		pack();
		Dimension contentPaneSize = getContentPane().getPreferredSize();
		setLocation(scrSize.width / 2 - contentPaneSize.width / 2,scrSize.height / 2 - contentPaneSize.height / 2);
		setResizable(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
	}
	public void setMaximumProgress(final int i) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.setMaximum(i);
				setVisible(true);
			}
		});
	}
	public void updateProgress(final int i) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.setValue(i);
				if (isComplete()) {
					if (hasMessages) {
						cancelButton.setText(Translator.localize("button.close"));
					}else {
						close();
					}
				}
			}
		});
	}
	private boolean isComplete() {
		return progress.getValue() == progress.getMaximum();
	}
	private static final long serialVersionUID = -1336242911879462274l;
	public void close() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(false);
				dispose();
			}
		});
	}
	public boolean isCanceled() {
		return canceled;
	}
	public void notifyMessage(final String title,final String introduction,final String message) {
		hasMessages = true;
		messageArea.setText(messageArea.getText() + title + "\n" + introduction + "\n" + message + "\n\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}
	public void notifyNullAction() {
		String msg = Translator.localize("label.import.empty");
		notifyMessage(msg,msg,msg);
	}
	public void updateMainTask(final String name) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setTitle(name);
			}
		});
	}
	public void updateSubTask(final String action) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressLabel.setText(action);
			}
		});
	}
	public void progress(ProgressEvent event)throws InterruptedException {
	}
	public void windowClosing(WindowEvent e) {
		canceled = true;
		close();
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {
	}
}



