package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.argouml.uml.diagram.state.ui.FigStateVertex;


public class FigFinalState extends FigStateVertex {
	private static final int WIDTH = 24;
	private static final int HEIGHT = 24;
	private FigCircle inCircle;
	private FigCircle outCircle;
	public FigFinalState(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initFigs();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigFinalState() {
		super();
		initFigs();
	}
	private void initFigs() {
		setEditable(false);
		Color handleColor = Globals.getPrefs().getHandleColor();
		FigCircle bigPort = new FigCircle(X0,Y0,WIDTH,HEIGHT,LINE_COLOR,FILL_COLOR);
		outCircle = new FigCircle(X0,Y0,WIDTH,HEIGHT,LINE_COLOR,FILL_COLOR);
		inCircle = new FigCircle(X0 + 5,Y0 + 5,WIDTH - 10,HEIGHT - 10,handleColor,LINE_COLOR);
		outCircle.setLineWidth(LINE_WIDTH);
		outCircle.setLineColor(LINE_COLOR);
		inCircle.setLineWidth(0);
		addFig(bigPort);
		addFig(outCircle);
		addFig(inCircle);
		setBigPort(bigPort);
		setBlinkPorts(false);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigFinalState(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
	}
	@Override public Object clone() {
		FigFinalState figClone = (FigFinalState) super.clone();
		Iterator it = figClone.getFigs().iterator();
		figClone.setBigPort((FigCircle) it.next());
		figClone.outCircle = (FigCircle) it.next();
		figClone.inCircle = (FigCircle) it.next();
		return figClone;
	}
	@Override public Selection makeSelection() {
		Object pstate = getOwner();
		Selection sel = null;
		if (pstate != null) {
			if (Model.getFacade().isAActivityGraph(Model.getFacade().getStateMachine(Model.getFacade().getContainer(pstate)))) {
				sel = new SelectionActionState(this);
				((SelectionActionState) sel).setOutgoingButtonEnabled(false);
			}
		}
		return sel;
	}
	@Override public boolean isResizable() {
		return false;
	}
	@Override public void setLineColor(Color col) {
		outCircle.setLineColor(col);
		inCircle.setFillColor(col);
	}
	@Override public Color getLineColor() {
		return outCircle.getLineColor();
	}
	@Override public void setFillColor(Color col) {
		if (Color.black.equals(col)) {
			col = Color.white;
		}
		outCircle.setFillColor(col);
	}
	@Override public Color getFillColor() {
		return outCircle.getFillColor();
	}
	@Override public void setFilled(boolean f) {
	}
	@Override public boolean isFilled() {
		return true;
	}
	@Override public void setLineWidth(int w) {
		outCircle.setLineWidth(w);
	}
	@Override public int getLineWidth() {
		return outCircle.getLineWidth();
	}
	@Override public void mouseClicked(MouseEvent me) {
	}
	static final long serialVersionUID = -3506578343969467480l;
	@Override public List getGravityPoints() {
		return getCircleGravityPoints();
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		if (getNameFig() == null) {
			return;
		}
		Rectangle oldBounds = getBounds();
		getBigPort().setBounds(x,y,w,h);
		outCircle.setBounds(x,y,w,h);
		inCircle.setBounds(x + 5,y + 5,w - 10,h - 10);
		calcBounds();
		updateEdges();
		firePropChange("bounds",oldBounds,getBounds());
	}
}



