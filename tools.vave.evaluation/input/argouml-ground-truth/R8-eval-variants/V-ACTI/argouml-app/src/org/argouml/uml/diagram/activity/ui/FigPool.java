package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigStereotypesGroup;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;


public class FigPool extends FigNodeModelElement {
	@SuppressWarnings("deprecation")@Deprecated public FigPool(Rectangle r) {
		initialize(r);
	}
	private void initialize(Rectangle r) {
		setBigPort(new FigEmptyRect(r.x,r.y,r.width,r.height));
		getBigPort().setFilled(false);
		getBigPort().setLineWidth(0);
		addFig(getBigPort());
		setBounds(r);
	}
	public FigPool(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(null,bounds,settings);
		initialize(bounds);
	}
	protected FigStereotypesGroup createStereotypeFig() {
		return null;
	}
	@Override public Object clone() {
		FigPool figClone = (FigPool) super.clone();
		Iterator it = figClone.getFigs().iterator();
		figClone.setBigPort((FigRect) it.next());
		return figClone;
	}
	@Override public void addEnclosedFig(Fig figState) {
		super.addEnclosedFig(figState);
		Iterator it = getLayer().getContentsNoEdges().iterator();
		while (it.hasNext()) {
			Fig f = (Fig) it.next();
			if (f instanceof FigPartition&&f.getBounds().intersects(figState.getBounds())) {
				Model.getCoreHelper().setModelElementContainer(figState.getOwner(),f.getOwner());
			}
		}
	}
	@Override public void setFillColor(Color col) {
		getBigPort().setFillColor(col);
		getNameFig().setFillColor(col);
	}
	@Override public Color getFillColor() {
		return getBigPort().getFillColor();
	}
	@Override public void setFilled(boolean f) {
		getBigPort().setFilled(f);
	}
	@Override public boolean isFilled() {
		return getBigPort().isFilled();
	}
	@Override public Dimension getMinimumSize() {
		Dimension nameDim = getNameFig().getMinimumSize();
		int w = nameDim.width;
		int h = nameDim.height;
		w = Math.max(64,w);
		h = Math.max(256,h);
		return new Dimension(w,h);
	}
	@Override public boolean getUseTrapRect() {
		return true;
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		Rectangle oldBounds = getBounds();
		getBigPort().setBounds(x,y,w,h);
		firePropChange("bounds",oldBounds,getBounds());
		calcBounds();
	}
	@Override public boolean isSelectable() {
		return false;
	}
}



