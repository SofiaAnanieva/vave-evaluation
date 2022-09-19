package org.argouml.uml.diagram.ui;

import java.util.List;
import java.awt.Point;
import java.awt.event.MouseEvent;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.PathItemPlacementStrategy;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;


public class ModeLabelDrag extends FigModifyingModeImpl {
	private Fig dragFig = null;
	private FigEdge figEdge = null;
	private Point dragBasePoint = new Point(0,0);
	private int deltax = 0;
	private int deltay = 0;
	public ModeLabelDrag(Editor editor) {
		super(editor);
	}
	public ModeLabelDrag() {
		super();
	}
	public String instructions() {
		return"  ";
	}
	public void mousePressed(MouseEvent me) {
		Point clickPoint = me.getPoint();
		Fig underMouse = editor.hit(clickPoint);
		if (underMouse instanceof FigEdge) {
			List<Fig>figList = ((FigEdge) underMouse).getPathItemFigs();
			for (Fig fig:figList) {
				if (fig.contains(clickPoint)) {
					me.consume();
					dragFig = fig;
					dragBasePoint = fig.getCenter();
					deltax = clickPoint.x - dragBasePoint.x;
					deltay = clickPoint.y - dragBasePoint.y;
					figEdge = (FigEdge) underMouse;
					break;
				}
			}
		}
	}
	public void mouseReleased(MouseEvent me) {
		if (dragFig != null) {
			dragFig = null;
		}
	}
	public void mouseDragged(MouseEvent me) {
		if (dragFig != null) {
			me = editor.translateMouseEvent(me);
			Point newPoint = me.getPoint();
			newPoint.translate(-deltax,-deltay);
			PathItemPlacementStrategy pips = figEdge.getPathItemPlacementStrategy(dragFig);
			pips.setPoint(newPoint);
			newPoint = pips.getPoint();
			int dx = newPoint.x - dragBasePoint.x;
			int dy = newPoint.y - dragBasePoint.y;
			dragBasePoint.setLocation(newPoint);
			dragFig.translate(dx,dy);
			me.consume();
			editor.damaged(dragFig);
		}
	}
}



