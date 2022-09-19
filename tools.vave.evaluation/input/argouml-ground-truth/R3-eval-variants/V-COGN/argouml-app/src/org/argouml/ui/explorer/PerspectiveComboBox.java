package org.argouml.ui.explorer;

import javax.swing.JComboBox;


public class PerspectiveComboBox extends JComboBox implements PerspectiveManagerListener {
	public PerspectiveComboBox() {
		this.setMaximumRowCount(9);
		PerspectiveManager.getInstance().addListener(this);
	}
	public void addPerspective(Object perspective) {
		addItem(perspective);
	}
	public void removePerspective(Object perspective) {
		removeItem(perspective);
	}
}



