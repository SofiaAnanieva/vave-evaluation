package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.FigDiamond;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;


public class FigNodeAssociation extends FigNodeModelElement {
	private static final int X = 0;
	private static final int Y = 0;
	private FigDiamond head;
	@SuppressWarnings("deprecation")@Deprecated public FigNodeAssociation() {
		super();
		initFigs();
	}
	private void initFigs() {
		setEditable(false);
		setBigPort(new FigDiamond(0,0,70,70,DEBUG_COLOR,DEBUG_COLOR));
		head = new FigDiamond(0,0,70,70,LINE_COLOR,FILL_COLOR);
		getNameFig().setFilled(false);
		getNameFig().setLineWidth(0);
		getStereotypeFig().setBounds(X + 10,Y + NAME_FIG_HEIGHT + 1,0,NAME_FIG_HEIGHT);
		getStereotypeFig().setFilled(false);
		getStereotypeFig().setLineWidth(0);
		addFig(getBigPort());
		addFig(head);
		if (!Model.getFacade().isAAssociationClass(getOwner())) {
			addFig(getNameFig());
			addFig(getStereotypeFig());
		}
		setBlinkPorts(false);
		Rectangle r = getBounds();
		setBounds(r);
		setResizable(true);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigNodeAssociation(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
	}
	public FigNodeAssociation(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initFigs();
	}
	@Override public Object clone() {
		FigNodeAssociation figClone = (FigNodeAssociation) super.clone();
		Iterator it = figClone.getFigs().iterator();
		figClone.setBigPort((FigDiamond) it.next());
		figClone.head = (FigDiamond) it.next();
		figClone.setNameFig((FigText) it.next());
		return figClone;
	}
	@Override protected void updateLayout(UmlChangeEvent mee) {
		super.updateLayout(mee);
		if (mee.getSource() == getOwner()&&mee instanceof RemoveAssociationEvent&&"connection".equals(mee.getPropertyName())&&Model.getFacade().getConnections(getOwner()).size() == 2) {
			reduceToBinary();
		}
	}
	private void reduceToBinary() {
		final Object association = getOwner();
		assert(Model.getFacade().getConnections(association).size() == 2);
		final Collection<FigEdge>existingEdges = getEdges();
		for (Iterator<FigEdge>it = existingEdges.iterator();it.hasNext();) {
			FigEdge edge = it.next();
			if (edge instanceof FigAssociationEnd) {
				it.remove();
			}else {
				removeFigEdge(edge);
			}
		}
		final LayerPerspective lay = (LayerPerspective) getLayer();
		final MutableGraphModel gm = (MutableGraphModel) lay.getGraphModel();
		gm.removeNode(association);
		removeFromDiagram();
		final GraphEdgeRenderer renderer = lay.getGraphEdgeRenderer();
		final FigAssociation figEdge = (FigAssociation) renderer.getFigEdgeFor(gm,lay,association,null);
		lay.add(figEdge);
		gm.addEdge(association);
		for (FigEdge edge:existingEdges) {
			figEdge.makeEdgePort();
			if (edge.getDestFigNode() == this) {
				edge.setDestFigNode(figEdge.getEdgePort());
				edge.setDestPortFig(figEdge.getEdgePort());
			}
			if (edge.getSourceFigNode() == this) {
				edge.setSourceFigNode(figEdge.getEdgePort());
				edge.setSourcePortFig(figEdge.getEdgePort());
			}
		}
		figEdge.computeRoute();
	}
	@Override public List getGravityPoints() {
		return getBigPort().getGravityPoints();
	}
	@Override public void setLineColor(Color col) {
		head.setLineColor(col);
	}
	@Override public Color getLineColor() {
		return head.getLineColor();
	}
	@Override public void setFillColor(Color col) {
		head.setFillColor(col);
	}
	@Override public Color getFillColor() {
		return head.getFillColor();
	}
	@Override public void setFilled(boolean f) {
	}
	@Override public boolean isFilled() {
		return true;
	}
	@Override public void setLineWidth(int w) {
		head.setLineWidth(w);
	}
	@Override public int getLineWidth() {
		return head.getLineWidth();
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		Rectangle oldBounds = getBounds();
		Rectangle nm = getNameFig().getBounds();
		getNameFig().setBounds(x + (w - nm.width) / 2,y + h / 2 - nm.height / 2,nm.width,nm.height);
		if (getStereotypeFig().isVisible()) {
			getStereotypeFig().setBounds(x,y + h / 2 - 20,w,15);
			int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
			getStereotypeFig().setBounds(x,y,w,stereotypeHeight);
		}
		head.setBounds(x,y,w,h);
		getBigPort().setBounds(x,y,w,h);
		calcBounds();
		firePropChange("bounds",oldBounds,getBounds());
		updateEdges();
	}
	@Override public Dimension getMinimumSize() {
		Dimension aSize = getNameFig().getMinimumSize();
		if (getStereotypeFig().isVisible()) {
			Dimension stereoMin = getStereotypeFig().getMinimumSize();
			aSize.width = Math.max(aSize.width,stereoMin.width);
			aSize.height += stereoMin.height;
		}
		aSize.width = Math.max(70,aSize.width);
		int size = Math.max(aSize.width,aSize.height);
		aSize.width = size;
		aSize.height = size;
		return aSize;
	}
	@Override protected void removeFromDiagramImpl() {
		FigEdgeAssociationClass figEdgeLink = null;
		final List edges = getFigEdges();
		if (edges != null) {
			for (Iterator it = edges.iterator();it.hasNext()&&figEdgeLink == null;) {
				Object o = it.next();
				if (o instanceof FigEdgeAssociationClass) {
					figEdgeLink = (FigEdgeAssociationClass) o;
				}
			}
		}
		if (figEdgeLink != null) {
			FigNode figClassBox = figEdgeLink.getDestFigNode();
			if (!(figClassBox instanceof FigClassAssociationClass)) {
				figClassBox = figEdgeLink.getSourceFigNode();
			}
			figEdgeLink.removeFromDiagramImpl();
			((FigClassAssociationClass) figClassBox).removeFromDiagramImpl();
		}
		super.removeFromDiagramImpl();
	}
}



