package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;
import org.tigris.gef.ui.IStatusBar;


public class StatusBar extends JPanel implements Runnable,IStatusBar {
	private JLabel msg = new JLabel();
	private JProgressBar progress = new JProgressBar();
	private String statusText;
	public StatusBar() {
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setMinimumSize(new Dimension(100,20));
		progress.setSize(new Dimension(100,20));
		msg.setMinimumSize(new Dimension(300,20));
		msg.setSize(new Dimension(300,20));
		msg.setFont(new Font("Dialog",Font.PLAIN,10));
		msg.setForeground(Color.black);
		setLayout(new BorderLayout());
		setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		add(msg,BorderLayout.CENTER);
		add(progress,BorderLayout.EAST);
	}
	public void showStatus(String s) {
		msg.setText(s);
		paintImmediately(getBounds());
	}
	public void showProgress(int percent) {
		progress.setValue(percent);
	}
	public void incProgress(int delataPercent) {
		progress.setValue(progress.getValue() + delataPercent);
	}
	public synchronized void doFakeProgress(String s,int work) {
		statusText = s;
		showStatus(statusText + "... not implemented yet ...");
		progress.setMaximum(work);
		progress.setValue(0);
		Thread t = new Thread(this);
		t.start();
	}
	public synchronized void run() {
		int work = progress.getMaximum();
		for (int i = 0;i < work;i++) {
			progress.setValue(i);
			repaint();
			try {
				wait(10);
			}catch (InterruptedException ex) {
			}
		}
		showStatus(statusText + "... done.");
		repaint();
		try {
			wait(1000);
		}catch (InterruptedException ex) {
		}
		progress.setValue(0);
		showStatus("");
		repaint();
	}
}



