package org.argouml.uml.diagram.static_structure.layout;

import java.awt.Point;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


public class ClassdiagramAssociationEdge extends ClassdiagramEdge {
	public ClassdiagramAssociationEdge(FigEdge edge) {
		super(edge);
	}
	private static final int SELF_SIZE = 30;
	public void layout() {
		if (getDestFigNode() == getSourceFigNode()) {
			Point centerRight = getCenterRight((FigNode) getSourceFigNode());
			int yoffset = getSourceFigNode().getHeight() / 2;
			yoffset = java.lang.Math.min(SELF_SIZE,yoffset);
			FigPoly fig = getUnderlyingFig();
			fig.addPoint(centerRight);
			fig.addPoint(centerRight.x + SELF_SIZE,centerRight.y);
			fig.addPoint(centerRight.x + SELF_SIZE,centerRight.y + yoffset);
			fig.addPoint(centerRight.x,centerRight.y + yoffset);
			fig.setFilled(false);
			fig.setSelfLoop(true);
			getCurrentEdge().setFig(fig);
		}
	}
	private Point getCenterRight(FigNode fig) {
		Point center = fig.getCenter();
		return new Point(center.x + fig.getWidth() / 2,center.y);
	}
}



