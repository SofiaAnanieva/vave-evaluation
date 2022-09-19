package org.argouml.uml.diagram.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;


public class FigTextGroup extends ArgoFigGroup implements MouseListener {
	private boolean supressCalcBounds = false;
	@SuppressWarnings("deprecation")@Deprecated public FigTextGroup() {
		super();
	}
	public FigTextGroup(Object owner,DiagramSettings settings) {
		super(owner,settings);
	}
	@Override public void addFig(Fig f) {
		super.addFig(f);
		updateFigTexts();
		calcBounds();
	}
	private void updateFigTexts() {
		int height = 0;
		for (Fig fig:(List<Fig>) getFigs()) {
			int figHeight = fig.getMinimumSize().height;
			fig.setBounds(getX(),getY() + height,fig.getWidth(),figHeight);
			fig.endTrans();
			height += fig.getHeight();
		}
	}
	@Override public void calcBounds() {
		updateFigTexts();
		if (!supressCalcBounds) {
			super.calcBounds();
			int maxWidth = 0;
			int height = 0;
			for (Fig fig:(List<Fig>) getFigs()) {
				if (fig.getWidth() > maxWidth) {
					maxWidth = fig.getWidth();
				}
				fig.setHeight(fig.getMinimumSize().height);
				height += fig.getHeight();
			}
			_w = maxWidth;
			_h = height;
		}
	}
	@Override public void removeFromDiagram() {
		for (Fig fig:(List<Fig>) getFigs()) {
			fig.removeFromDiagram();
		}
		super.removeFromDiagram();
	}
	@Override public void deleteFromModel() {
		for (Fig fig:(List<Fig>) getFigs()) {
			fig.deleteFromModel();
		}
		super.deleteFromModel();
	}
	public void mousePressed(MouseEvent me) {
	}
	public void mouseReleased(MouseEvent me) {
	}
	public void mouseEntered(MouseEvent me) {
	}
	public void mouseExited(MouseEvent me) {
	}
	public void mouseClicked(MouseEvent me) {
		if (me.isConsumed()) {
			return;
		}
		if (me.getClickCount() >= 2) {
			Fig f = hitFig(new Rectangle(me.getX() - 2,me.getY() - 2,4,4));
			if (f instanceof MouseListener) {
				((MouseListener) f).mouseClicked(me);
			}
			if (me.isConsumed()) {
				return;
			}
			for (Object o:this.getFigs()) {
				f = (Fig) o;
				if (f instanceof MouseListener&&f instanceof FigText) {
					if (((FigText) f).getEditable()) {
						((MouseListener) f).mouseClicked(me);
					}
				}
			}
		}
		me.consume();
	}
	public boolean hit(Rectangle r) {
		return this.intersects(r);
	}
	public boolean contains(int x,int y) {
		return(_x <= x)&&(x <= _x + _w)&&(_y <= y)&&(y <= _y + _h);
	}
}



