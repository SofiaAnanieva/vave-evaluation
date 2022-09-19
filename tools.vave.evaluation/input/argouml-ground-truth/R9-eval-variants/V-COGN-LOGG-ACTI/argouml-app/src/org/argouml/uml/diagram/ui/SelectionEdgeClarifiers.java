package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.base.SelectionReshape;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.argouml.uml.diagram.ui.Clarifiable;


public class SelectionEdgeClarifiers extends SelectionReshape {
	public SelectionEdgeClarifiers(Fig f) {
		super(f);
	}
	@Override public void paint(Graphics g) {
		super.paint(g);
		int selectionCount = Globals.curEditor().getSelectionManager().getSelections().size();
		if (selectionCount == 1) {
			FigEdge edge = (FigEdge) getContent();
			if (edge instanceof Clarifiable) {
				((Clarifiable) edge).paintClarifiers(g);
			}
			for (PathItemPlacementStrategy strategy:edge.getPathItemStrategies()) {
				strategy.paint(g);
			}
		}
	}
}



