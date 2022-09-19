package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;
import org.tigris.gef.ui.IStatusBar;


public class SplashScreen extends JWindow implements IStatusBar {
	private StatusBar statusBar = new StatusBar();
	private boolean paintCalled = false;
	public SplashScreen() {
		this("Loading ArgoUML...","Splash");
	}
	private SplashScreen(String title,String iconName) {
		super();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getContentPane().setLayout(new BorderLayout(0,0));
		SplashPanel panel = new SplashPanel(iconName);
		if (panel.getImage() != null) {
			int imgWidth = panel.getImage().getIconWidth();
			int imgHeight = panel.getImage().getIconHeight();
			Point scrCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
			setLocation(scrCenter.x - imgWidth / 2,scrCenter.y - imgHeight / 2);
		}
		JPanel splash = new JPanel(new BorderLayout());
		splash.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		splash.add(panel,BorderLayout.CENTER);
		splash.add(statusBar,BorderLayout.SOUTH);
		getContentPane().add(splash);
		Dimension contentPaneSize = getContentPane().getPreferredSize();
		setSize(contentPaneSize.width,contentPaneSize.height);
		pack();
	}
	public StatusBar getStatusBar() {
		return statusBar;
	}
	public void showStatus(String s) {
		statusBar.showStatus(s);
	}
	@Override public void paint(Graphics g) {
		super.paint(g);
		if (!paintCalled) {
			synchronized (this) {
				paintCalled = true;
				notifyAll();
			}
		}
	}
	public void setPaintCalled(boolean called) {
		this.paintCalled = called;
	}
	public boolean isPaintCalled() {
		return paintCalled;
	}
}



