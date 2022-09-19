package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.model.InvalidElementException;
import org.argouml.notation.NotationProvider;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation;
import org.argouml.uml.diagram.ui.CompartmentFigText;


public abstract class FigEditableCompartment extends FigCompartment {
	private static final int MIN_HEIGHT = FigNodeModelElement.NAME_FIG_HEIGHT;
	private FigSeperator compartmentSeperator;
	@SuppressWarnings("deprecation")@Deprecated public FigEditableCompartment(int x,int y,int w,int h) {
		super(x,y,w,h);
		constructFigs();
	}
	private void constructFigs() {
		compartmentSeperator = new FigSeperator(X0,Y0,11);
		addFig(compartmentSeperator);
	}
	public FigEditableCompartment(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		constructFigs();
	}
	protected FigSeperator getSeperatorFig() {
		return compartmentSeperator;
	}
	@Override public void setVisible(boolean visible) {
		if (isVisible() == visible) {
			return;
		}
		super.setVisible(visible);
		if (visible) {
			populate();
		}else {
			for (int i = getFigs().size() - 1;i >= 0;--i) {
				Fig f = getFigAt(i);
				if (f instanceof CompartmentFigText) {
					removeFig(f);
				}
			}
		}
	}
	@Override public void addFig(Fig fig) {
		if (fig != getBigPort()&&!(fig instanceof CompartmentFigText)&&!(fig instanceof FigSeperator)) {
			throw new IllegalArgumentException("A FigEditableCompartment can only " + "contain CompartmentFigTexts, " + "received a " + fig.getClass().getName());
		}
		super.addFig(fig);
	}
	protected abstract Collection getUmlCollection();
	protected abstract int getNotationType();
	public void populate() {
		if (!isVisible()) {
			return;
		}
		Fig bigPort = this.getBigPort();
		int xpos = bigPort.getX();
		int ypos = bigPort.getY();
		List<Fig>figs = getElementFigs();
		for (Fig f:figs) {
			removeFig(f);
		}
		FigSingleLineTextWithNotation comp = null;
		try {
			int acounter = -1;
			for (Object umlObject:getUmlCollection()) {
				comp = findCompartmentFig(figs,umlObject);
				acounter++;
				if (comp == null) {
					comp = createFigText(umlObject,new Rectangle(xpos + 1,ypos + 1 + acounter * ROWHEIGHT,0,ROWHEIGHT - 2),getSettings());
				}else {
					Rectangle b = comp.getBounds();
					b.y = ypos + 1 + acounter * ROWHEIGHT;
					comp.setBounds(b);
				}
				comp.initNotationProviders();
				addFig(comp);
				String ftText = comp.getNotationProvider().toString(umlObject,comp.getNotationSettings());
				if (ftText == null) {
					ftText = "";
				}
				comp.setText(ftText);
				comp.setBotMargin(0);
			}
		}catch (InvalidElementException e) {
		}
		if (comp != null) {
			comp.setBotMargin(6);
		}
	}
	private CompartmentFigText findCompartmentFig(List<Fig>figs,Object umlObject) {
		for (Fig fig:figs) {
			if (fig instanceof CompartmentFigText) {
				CompartmentFigText candidate = (CompartmentFigText) fig;
				if (candidate.getOwner() == umlObject) {
					return candidate;
				}
			}
		}
		return null;
	}
	private List<Fig>getElementFigs() {
		List<Fig>figs = new ArrayList<Fig>(getFigs());
		if (figs.size() > 1) {
			figs.remove(1);
			figs.remove(0);
		}
		return figs;
	}
	@Deprecated protected FigSingleLineTextWithNotation createFigText(int x,int y,int w,int h,Fig aFig,NotationProvider np) {
		return null;
	}
	@SuppressWarnings("deprecation")protected FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,@SuppressWarnings("unused")DiagramSettings settings,NotationProvider np) {
		FigSingleLineTextWithNotation comp = createFigText(bounds.x,bounds.y,bounds.width,bounds.height,this.getBigPort(),np);
		comp.setOwner(owner);
		return comp;
	}
	abstract FigSingleLineTextWithNotation createFigText(Object owner,Rectangle bounds,DiagramSettings settings);
	public Dimension updateFigGroupSize(int x,int y,int w,int h,boolean checkSize,int rowHeight) {
		return getMinimumSize();
	}
	@Override public Dimension getMinimumSize() {
		Dimension d = super.getMinimumSize();
		if (d.height < MIN_HEIGHT) {
			d.height = MIN_HEIGHT;
		}
		return d;
	}
	@Override protected void setBoundsImpl(int x,int y,int w,int h) {
		int newW = w;
		int newH = h;
		int fw;
		int yy = y;
		int lineWidth = getLineWidth();
		for (Fig fig:(List<Fig>) getFigs()) {
			if (fig.isVisible()&&fig != getBigPort()) {
				if (fig instanceof FigSeperator) {
					fw = w;
				}else {
					fw = fig.getMinimumSize().width;
				}
				fig.setBounds(x + lineWidth,yy + lineWidth,fw,fig.getMinimumSize().height);
				if (newW < fw + 2 * lineWidth) {
					newW = fw + 2 * lineWidth;
				}
				yy += fig.getMinimumSize().height;
			}
		}
		getBigPort().setBounds(x + lineWidth,y + lineWidth,newW - 2 * lineWidth,newH - 2 * lineWidth);
		calcBounds();
	}
	protected static class FigSeperator extends FigLine {
	FigSeperator(int x,int y,int len) {
			super(x,y,(x + len) - 1,y,LINE_COLOR);
			setLineWidth(LINE_WIDTH);
		}
	@Override public Dimension getSize() {
		return new Dimension((_x2 - _x1) + 1,getLineWidth());
	}
	@Override public Dimension getMinimumSize() {
		return new Dimension(0,getLineWidth());
	}
	@Override public void setBoundsImpl(int x,int y,int w,int h) {
		setX1(x);
		setY1(y);
		setX2((x + w) - 1);
		setY2(y);
	}
	private static final long serialVersionUID = -2222511596507221760l;
}
}



