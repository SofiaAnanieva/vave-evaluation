package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBoxWithAttributes;


public class FigClass extends FigClassifierBoxWithAttributes {
	@Deprecated public FigClass(Object modelElement,int x,int y,int w,int h) {
		this(null,modelElement);
		setBounds(x,y,w,h);
	}
	@Deprecated public FigClass(GraphModel gm,Object node) {
		super();
		setOwner(node);
		constructFigs();
	}
	private void constructFigs() {
		addFig(getBigPort());
		addFig(getStereotypeFig());
		addFig(getNameFig());
		addFig(getOperationsFig());
		addFig(getAttributesFig());
		addFig(borderFig);
	}
	public FigClass(Object element,Rectangle bounds,DiagramSettings settings) {
		super(element,bounds,settings);
		constructFigs();
		Rectangle r = getBounds();
		setStandardBounds(r.x,r.y,r.width,r.height);
	}
	@Override public Object clone() {
		FigClass figClone = (FigClass) super.clone();
		Iterator thisIter = this.getFigs().iterator();
		Iterator cloneIter = figClone.getFigs().iterator();
		while (thisIter.hasNext()) {
			Fig thisFig = (Fig) thisIter.next();
			Fig cloneFig = (Fig) cloneIter.next();
			if (thisFig == borderFig) {
				figClone.borderFig = thisFig;
			}
		}
		return figClone;
	}
	public Selection makeSelection() {
		return new SelectionClass(this);
	}
	protected Object buildModifierPopUp() {
		return buildModifierPopUp(ABSTRACT|LEAF|ROOT|ACTIVE);
	}
	public int getLineWidth() {
		return borderFig.getLineWidth();
	}
	protected FigText getPreviousVisibleFeature(FigGroup fgVec,FigText ft,int i) {
		if (fgVec == null||i < 1) {
			return null;
		}
		FigText ft2 = null;
		List figs = fgVec.getFigs();
		if (i >= figs.size()||!((FigText) figs.get(i)).isVisible()) {
			return null;
		}
		do {
			i--;
			while (i < 1) {
				if (fgVec == getAttributesFig()) {
					fgVec = getOperationsFig();
				}else {
					fgVec = getAttributesFig();
				}
				figs = fgVec.getFigs();
				i = figs.size() - 1;
			}
			ft2 = (FigText) figs.get(i);
			if (!ft2.isVisible()) {
				ft2 = null;
			}
		}while (ft2 == null);
		return ft2;
	}
	protected FigText getNextVisibleFeature(FigGroup fgVec,FigText ft,int i) {
		if (fgVec == null||i < 1) {
			return null;
		}
		FigText ft2 = null;
		List v = fgVec.getFigs();
		if (i >= v.size()||!((FigText) v.get(i)).isVisible()) {
			return null;
		}
		do {
			i++;
			while (i >= v.size()) {
				if (fgVec == getAttributesFig()) {
					fgVec = getOperationsFig();
				}else {
					fgVec = getAttributesFig();
				}
				v = new ArrayList(fgVec.getFigs());
				i = 1;
			}
			ft2 = (FigText) v.get(i);
			if (!ft2.isVisible()) {
				ft2 = null;
			}
		}while (ft2 == null);
		return ft2;
	}
	public void setEnclosingFig(Fig encloser) {
		if (encloser == getEncloser()) {
			return;
		}
		if (encloser == null||(encloser != null&&!Model.getFacade().isAInstance(encloser.getOwner()))) {
			super.setEnclosingFig(encloser);
		}
		if (!(Model.getFacade().isAUMLElement(getOwner()))) {
			return;
		}
		if (encloser != null&&(Model.getFacade().isAComponent(encloser.getOwner()))) {
			moveIntoComponent(encloser);
			super.setEnclosingFig(encloser);
		}
	}
	@Override protected void updateNameText() {
		super.updateNameText();
		calcBounds();
		setBounds(getBounds());
	}
}



