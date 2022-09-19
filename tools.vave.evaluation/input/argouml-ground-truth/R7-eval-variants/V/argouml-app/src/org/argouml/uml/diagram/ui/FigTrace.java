package org.argouml.uml.diagram.ui;

import java.awt.Color;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.FigEdgeLine;


public class FigTrace extends FigEdgeLine {
	public FigTrace() {
		getFig().setLineColor(Color.red);
		ArrowHeadTriangle endArrow = new ArrowHeadTriangle();
		endArrow.setFillColor(Color.red);
		setDestArrowHead(endArrow);
		setBetweenNearestPoints(true);
	}
	public FigTrace(Object edge) {
		this();
		setOwner(edge);
	}
	static final long serialVersionUID = -2094146244090391544l;
}



