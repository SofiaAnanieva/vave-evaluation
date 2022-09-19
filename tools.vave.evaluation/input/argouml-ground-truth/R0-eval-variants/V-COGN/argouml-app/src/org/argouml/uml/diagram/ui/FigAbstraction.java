package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadTriangle;


public class FigAbstraction extends FigDependency {
	@SuppressWarnings("deprecation")@Deprecated public FigAbstraction() {
		super();
		setDestArrowHead(createEndArrow());
	}
	protected ArrowHead createEndArrow() {
		final ArrowHead arrow = new ArrowHeadTriangle();
		arrow.setFillColor(FILL_COLOR);
		return arrow;
	}
	@SuppressWarnings("deprecation")@Deprecated public FigAbstraction(Object edge) {
		this();
		setOwner(edge);
	}
	public FigAbstraction(Object owner,DiagramSettings settings) {
		super(owner,settings);
	}
}



