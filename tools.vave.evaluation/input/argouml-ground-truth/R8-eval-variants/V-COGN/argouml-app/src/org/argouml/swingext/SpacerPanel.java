package org.argouml.swingext;

import java.awt.Dimension;
import javax.swing.JPanel;


public class SpacerPanel extends JPanel {
	private int w = 10,h = 10;
	public SpacerPanel() {
	}
	public SpacerPanel(int width,int height) {
		w = width;
		h = height;
	}
	public Dimension getMinimumSize() {
		return new Dimension(w,h);
	}
	public Dimension getPreferredSize() {
		return new Dimension(w,h);
	}
	public Dimension getSize() {
		return new Dimension(w,h);
	}
}



