package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.argouml.notation.NotationProvider;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigSingleLineTextWithNotation;


public class CompartmentFigText extends FigSingleLineTextWithNotation implements TargetListener {
	private static final int MARGIN = 3;
	private static final long serialVersionUID = 3830572062785308980l;
	private static final Logger LOG = Logger.getLogger(CompartmentFigText.class);
	@Deprecated private Fig refFig;
	private boolean highlighted;
	@SuppressWarnings("deprecation")@Deprecated public CompartmentFigText(int x,int y,int w,int h,Fig aFig,NotationProvider np) {
		super(x,y,w,h,true);
		if (np == null) {
			LOG.warn("Need a NotationProvider for CompartmentFigText.");
		}
		setNotationProvider(np);
		refFig = aFig;
		if (refFig == null) {
			LOG.warn(this.getClass().toString() + ": Cannot create with null compartment fig");
		}
		setJustification(FigText.JUSTIFY_LEFT);
		setRightMargin(MARGIN);
		setLeftMargin(MARGIN);
	}
	@Deprecated public CompartmentFigText(Object element,Rectangle bounds,DiagramSettings settings,NotationProvider np) {
		super(element,bounds,settings,true);
		if (np == null) {
			LOG.warn("Need a NotationProvider for CompartmentFigText.");
		}
		setNotationProvider(np);
		setJustification(FigText.JUSTIFY_LEFT);
		setRightMargin(MARGIN);
		setLeftMargin(MARGIN);
	}
	public CompartmentFigText(Object element,Rectangle bounds,DiagramSettings settings) {
		super(element,bounds,settings,true);
		TargetManager.getInstance().addTargetListener(this);
		setJustification(FigText.JUSTIFY_LEFT);
		setRightMargin(MARGIN);
		setLeftMargin(MARGIN);
	}
	@Deprecated public CompartmentFigText(int x,int y,int w,int h,Fig aFig,String property) {
		this(x,y,w,h,aFig,new String[] {property});
	}
	@SuppressWarnings("deprecation")@Deprecated public CompartmentFigText(int x,int y,int w,int h,Fig aFig,String[]properties) {
		super(x,y,w,h,true,properties);
		if (aFig == null) {
			throw new IllegalArgumentException("A refFig must be provided");
		}
		refFig = aFig;
	}
	public CompartmentFigText(Object owner,Rectangle bounds,DiagramSettings settings,String property) {
		this(owner,bounds,settings,new String[] {property});
	}
	public CompartmentFigText(Object owner,Rectangle bounds,DiagramSettings settings,String[]properties) {
		super(owner,bounds,settings,true,properties);
		TargetManager.getInstance().addTargetListener(this);
	}
	@Override public void removeFromDiagram() {
		super.removeFromDiagram();
		Fig fg = getGroup();
		if (fg instanceof FigGroup) {
			((FigGroup) fg).removeFig(this);
			setGroup(null);
		}
		TargetManager.getInstance().removeTargetListener(this);
	}
	@Override public boolean isFilled() {
		return false;
	}
	@Override public Color getLineColor() {
		if (refFig != null) {
			return refFig.getLineColor();
		}else {
			return super.getLineColor();
		}
	}
	public void setHighlighted(boolean flag) {
		highlighted = flag;
	}
	@Override public void paint(Graphics g) {
		super.paint(g);
		if (highlighted) {
			final int x = getX();
			final int y = getY();
			final int w = getWidth();
			final int h = getHeight();
			g.setColor(Globals.getPrefs().handleColorFor(this));
			g.drawRect(x - 1,y - 1,w + 2,h + 2);
			g.drawRect(x,y,w,h);
		}
	}
	public boolean isHighlighted() {
		return highlighted;
	}
	protected void textEdited() {
		setHighlighted(true);
		super.textEdited();
	}
	public void targetAdded(TargetEvent e) {
		if (Arrays.asList(e.getNewTargets()).contains(getOwner())) {
			setHighlighted(true);
			this.damage();
		}
	}
	public void targetRemoved(TargetEvent e) {
		if (e.getRemovedTargetCollection().contains(getOwner())) {
			setHighlighted(false);
			this.damage();
		}
	}
	public void targetSet(TargetEvent e) {
		setHighlighted((Arrays.asList(e.getNewTargets()).contains(getOwner())));
	}
}



