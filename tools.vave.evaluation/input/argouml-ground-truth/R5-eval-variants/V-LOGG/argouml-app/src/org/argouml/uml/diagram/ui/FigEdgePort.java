package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigEdge;
import org.argouml.uml.diagram.ui.FigNodeModelElement;


public class FigEdgePort extends FigNodeModelElement {
	private FigCircle bigPort;
	@SuppressWarnings("deprecation")@Deprecated public FigEdgePort() {
		super();
		initialize();
	}
	private void initialize() {
		invisibleAllowed = true;
		bigPort = new FigCircle(0,0,1,1,LINE_COLOR,FILL_COLOR);
		addFig(bigPort);
	}
	public FigEdgePort(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initialize();
	}
	@Override public boolean hit(Rectangle r) {
		return false;
	}
	@Override@Deprecated public void setOwner(Object own) {
		bigPort.setOwner(own);
		super.setOwner(own);
	}
	@Override public Object getOwner() {
		if (super.getOwner() != null) {
			return super.getOwner();
		}
		Fig group = this;
		while (group != null&&!(group instanceof FigEdge)) {
			group = group.getGroup();
		}
		if (group == null) {
			return null;
		}else {
			return group.getOwner();
		}
	}
	@Override public String classNameAndBounds() {
		return getClass().getName() + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + "]";
	}
	@Override public Object hitPort(int x,int y) {
		return null;
	}
	@Override public Fig hitFig(Rectangle r) {
		return null;
	}
	@Override public boolean isSelectable() {
		return false;
	}
	public Fig getPortFig(Object port) {
		return bigPort;
	}
	private static final long serialVersionUID = 3091219503512470458l;
}



