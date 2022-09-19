package org.argouml.application.api;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.argouml.i18n.Translator;
import org.argouml.util.ArgoFrame;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;


public abstract class AbstractArgoJPanel extends JPanel implements Cloneable,Orientable {
	private static final int OVERLAPP = 30;
	private String title = "untitled";
	private Icon icon = null;
	private boolean tear = false;
	private Orientation orientation;
	public Orientation getOrientation() {
		return orientation;
	}
	public AbstractArgoJPanel() {
		this(Translator.localize("tab.untitled"),false);
	}
	public AbstractArgoJPanel(String title) {
		this(title,false);
	}
	public AbstractArgoJPanel(String title,boolean t) {
		setTitle(title);
		tear = t;
	}
	public Object clone() {
		try {
			return this.getClass().newInstance();
		}catch (Exception ex) {
		}
		return null;
	}
	public void setOrientation(Orientation o) {
		this.orientation = o;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String t) {
		title = t;
	}
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon theIcon) {
		this.icon = theIcon;
	}
	public AbstractArgoJPanel spawn() {
		JDialog f = new JDialog(ArgoFrame.getInstance());
		f.getContentPane().setLayout(new BorderLayout());
		f.setTitle(Translator.localize(title));
		AbstractArgoJPanel newPanel = (AbstractArgoJPanel) clone();
		if (newPanel == null) {
			return null;
		}
		newPanel.setTitle(Translator.localize(title));
		f.getContentPane().add(newPanel,BorderLayout.CENTER);
		Rectangle bounds = getBounds();
		bounds.height += OVERLAPP * 2;
		f.setBounds(bounds);
		Point loc = new Point(0,0);
		SwingUtilities.convertPointToScreen(loc,this);
		loc.y -= OVERLAPP;
		f.setLocation(loc);
		f.setVisible(true);
		if (tear&&(getParent()instanceof JTabbedPane)) {
			((JTabbedPane) getParent()).remove(this);
		}
		return newPanel;
	}
}



