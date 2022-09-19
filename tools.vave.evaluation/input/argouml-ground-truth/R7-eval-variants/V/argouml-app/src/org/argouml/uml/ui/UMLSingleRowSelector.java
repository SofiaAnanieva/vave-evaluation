package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import org.argouml.uml.ui.ScrollList;


public class UMLSingleRowSelector extends JPanel {
	private JScrollPane scroll;
	private Dimension preferredSize = null;
	public UMLSingleRowSelector(ListModel model) {
		super(new BorderLayout());
		scroll = new ScrollList(model,1);
		add(scroll);
		preferredSize = scroll.getPreferredSize();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	public Dimension getMinimumSize() {
		Dimension size = super.getMinimumSize();
		size.height = preferredSize.height;
		return size;
	}
	public Dimension getMaximumSize() {
		Dimension size = super.getMaximumSize();
		size.height = preferredSize.height;
		return size;
	}
	public Dimension getPreferredSize() {
		return preferredSize;
	}
}



