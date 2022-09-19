package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;


public class FigSubactivityState extends FigStateVertex {
	private static final int PADDING = 8;
	private static final int X = X0;
	private static final int Y = Y0;
	private static final int W = 90;
	private static final int H = 25;
	private static final int SX = 3;
	private static final int SY = 3;
	private static final int SW = 9;
	private static final int SH = 5;
	private FigRRect cover;
	private FigRRect s1;
	private FigRRect s2;
	private FigLine s3;
	public FigSubactivityState(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initFigs();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigSubactivityState() {
		initFigs();
	}
	private void initFigs() {
		FigRRect bigPort = new FigRRect(X,Y,W,H,DEBUG_COLOR,DEBUG_COLOR);
		bigPort.setCornerRadius(bigPort.getHeight() / 2);
		cover = new FigRRect(X,Y,W,H,LINE_COLOR,FILL_COLOR);
		cover.setCornerRadius(getHeight() / 2);
		bigPort.setLineWidth(0);
		getNameFig().setLineWidth(0);
		getNameFig().setBounds(10 + PADDING,10,90 - PADDING * 2,25);
		getNameFig().setFilled(false);
		getNameFig().setReturnAction(FigText.INSERT);
		getNameFig().setEditable(false);
		addFig(bigPort);
		addFig(cover);
		addFig(getNameFig());
		makeSubStatesIcon(X + W,Y);
		setBigPort(bigPort);
		setBounds(getBounds());
	}
	private void makeSubStatesIcon(int x,int y) {
		s1 = new FigRRect(x - 22,y + 3,8,6,LINE_COLOR,FILL_COLOR);
		s2 = new FigRRect(x - 11,y + 9,8,6,LINE_COLOR,FILL_COLOR);
		s1.setFilled(true);
		s2.setFilled(true);
		s1.setLineWidth(LINE_WIDTH);
		s2.setLineWidth(LINE_WIDTH);
		s1.setCornerRadius(SH);
		s2.setCornerRadius(SH);
		s3 = new FigLine(x - 18,y + 6,x - 7,y + 12,LINE_COLOR);
		addFig(s3);
		addFig(s1);
		addFig(s2);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigSubactivityState(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
	}
	@Override public Object clone() {
		FigSubactivityState figClone = (FigSubactivityState) super.clone();
		Iterator it = figClone.getFigs().iterator();
		figClone.setBigPort((FigRRect) it.next());
		figClone.cover = (FigRRect) it.next();
		figClone.setNameFig((FigText) it.next());
		return figClone;
	}
	@Override public Dimension getMinimumSize() {
		Dimension nameDim = getNameFig().getMinimumSize();
		int w = nameDim.width + PADDING * 2;
		int h = nameDim.height + PADDING;
		return new Dimension(Math.max(w,W / 2),Math.max(h,H / 2));
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		if (getNameFig() == null) {
			return;
		}
		Rectangle oldBounds = getBounds();
		getNameFig().setBounds(x + PADDING,y,w - PADDING * 2,h - PADDING);
		getBigPort().setBounds(x,y,w,h);
		cover.setBounds(x,y,w,h);
		((FigRRect) getBigPort()).setCornerRadius(h);
		cover.setCornerRadius(h);
		s1.setBounds(x + w - 2 * (SX + SW),y + h - 1 * (SY + SH),SW,SH);
		s2.setBounds(x + w - 1 * (SX + SW),y + h - 2 * (SY + SH),SW,SH);
		s3.setShape(x + w - (SX * 2 + SW + SW / 2),y + h - (SY + SH / 2),x + w - (SX + SW / 2),y + h - (SY * 2 + SH + SH / 2));
		calcBounds();
		updateEdges();
		firePropChange("bounds",oldBounds,getBounds());
	}
	@Override public void setLineColor(Color col) {
		cover.setLineColor(col);
	}
	public Color getLineColor() {
		return cover.getLineColor();
	}
	public void setFillColor(Color col) {
		cover.setFillColor(col);
	}
	public Color getFillColor() {
		return cover.getFillColor();
	}
	@Override public void setFilled(boolean f) {
		cover.setFilled(f);
	}
	@Override public boolean isFilled() {
		return cover.isFilled();
	}
	@Override public void setLineWidth(int w) {
		cover.setLineWidth(w);
	}
	@Override public int getLineWidth() {
		return cover.getLineWidth();
	}
	@Override protected void modelChanged(PropertyChangeEvent mee) {
		super.modelChanged(mee);
		if (mee instanceof AssociationChangeEvent||mee instanceof AttributeChangeEvent) {
			renderingChanged();
			updateListeners(getOwner(),getOwner());
		}
	}
	@Override protected void updateListeners(Object oldOwner,Object newOwner) {
		Set<Object[]>l = new HashSet<Object[]>();
		if (newOwner != null) {
			l.add(new Object[] {newOwner,null});
			Object machine = Model.getFacade().getSubmachine(newOwner);
			if (machine != null) {
				l.add(new Object[] {machine,null});
			}
		}
		updateElementListeners(l);
	}
	@Override protected void updateNameText() {
		String s = "";
		if (getOwner() != null) {
			Object machine = Model.getFacade().getSubmachine(getOwner());
			if (machine != null) {
				s = Model.getFacade().getName(machine);
			}
		}
		if (s == null) {
			s = "";
		}
		getNameFig().setText(s);
	}
}



