package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import org.tigris.gef.base.SelectionMove;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.Clarifiable;


public class SelectionMoveClarifiers extends SelectionMove {
	public SelectionMoveClarifiers(Fig f) {
		super(f);
	}
	public void paint(Graphics g) {
		((Clarifiable) getContent()).paintClarifiers(g);
		super.paint(g);
	}
}



