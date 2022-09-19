package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.argouml.uml.diagram.PathContainer;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigEdgeAssociationClass;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;


public class FigAssociationClass extends FigAssociation implements AttributesCompartmentContainer,PathContainer,OperationsCompartmentContainer {
	private static final long serialVersionUID = 3643715304027095083l;
	@SuppressWarnings("deprecation")@Deprecated public FigAssociationClass() {
		super();
		setBetweenNearestPoints(true);
		((FigPoly) getFig()).setRectilinear(false);
		setDashed(false);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigAssociationClass(Object ed,Layer lay) {
		this();
		setLayer(lay);
		setOwner(ed);
	}
	public FigAssociationClass(Object element,DiagramSettings settings) {
		super(element,settings);
		setBetweenNearestPoints(true);
		((FigPoly) getFig()).setRectilinear(false);
		setDashed(false);
	}
	protected void createNameLabel(Object owner,DiagramSettings settings) {
	}
	@Override protected void removeFromDiagramImpl() {
		FigEdgeAssociationClass figEdgeLink = null;
		List edges = null;
		FigEdgePort figEdgePort = getEdgePort();
		if (figEdgePort != null) {
			edges = figEdgePort.getFigEdges();
		}
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
	@Override public void setFig(Fig f) {
		super.setFig(f);
		getFig().setDashed(false);
	}
	@Override protected FigText getNameFig() {
		return null;
	}
	public Rectangle getAttributesBounds() {
		if (getAssociationClass() != null) {
			return getAssociationClass().getAttributesBounds();
		}else {
			return new Rectangle(0,0,0,0);
		}
	}
	public boolean isAttributesVisible() {
		if (getAssociationClass() != null) {
			return getAssociationClass().isAttributesVisible();
		}else {
			return true;
		}
	}
	public void setAttributesVisible(boolean visible) {
		if (getAssociationClass() != null) {
			getAssociationClass().setAttributesVisible(visible);
		}
	}
	public boolean isPathVisible() {
		if (getAssociationClass() != null) {
			return getAssociationClass().isPathVisible();
		}else {
			return false;
		}
	}
	public void setPathVisible(boolean visible) {
		if (getAssociationClass() != null) {
			getAssociationClass().setPathVisible(visible);
		}
	}
	public Rectangle getOperationsBounds() {
		if (getAssociationClass() != null) {
			return getAssociationClass().getOperationsBounds();
		}else {
			return new Rectangle(0,0,0,0);
		}
	}
	public boolean isOperationsVisible() {
		if (getAssociationClass() != null) {
			return getAssociationClass().isOperationsVisible();
		}else {
			return true;
		}
	}
	public void setOperationsVisible(boolean visible) {
		if (getAssociationClass() != null) {
			getAssociationClass().setOperationsVisible(visible);
		}
	}
	@Override public void setFillColor(Color color) {
		if (getAssociationClass() != null) {
			getAssociationClass().setFillColor(color);
		}
	}
	@Override public Color getFillColor() {
		if (getAssociationClass() != null) {
			return getAssociationClass().getFillColor();
		}else {
			return FILL_COLOR;
		}
	}
	@Override public void setLineColor(Color arg0) {
		super.setLineColor(arg0);
		if (getAssociationClass() != null) {
			getAssociationClass().setLineColor(arg0);
		}
		if (getFigEdgeAssociationClass() != null) {
			getFigEdgeAssociationClass().setLineColor(arg0);
		}
	}
	public FigClassAssociationClass getAssociationClass() {
		FigEdgeAssociationClass figEdgeLink = null;
		List edges = null;
		FigEdgePort figEdgePort = this.getEdgePort();
		if (figEdgePort != null) {
			edges = figEdgePort.getFigEdges();
		}
		if (edges != null) {
			for (Iterator it = edges.iterator();it.hasNext()&&figEdgeLink == null;) {
				Object o = it.next();
				if (o instanceof FigEdgeAssociationClass) {
					figEdgeLink = (FigEdgeAssociationClass) o;
				}
			}
		}
		FigNode figClassBox = null;
		if (figEdgeLink != null) {
			figClassBox = figEdgeLink.getDestFigNode();
			if (!(figClassBox instanceof FigClassAssociationClass)) {
				figClassBox = figEdgeLink.getSourceFigNode();
			}
		}
		return(FigClassAssociationClass) figClassBox;
	}
	public FigEdgeAssociationClass getFigEdgeAssociationClass() {
		FigEdgeAssociationClass figEdgeLink = null;
		List edges = null;
		FigEdgePort figEdgePort = this.getEdgePort();
		if (figEdgePort != null) {
			edges = figEdgePort.getFigEdges();
		}
		if (edges != null) {
			for (Iterator it = edges.iterator();it.hasNext()&&figEdgeLink == null;) {
				Object o = it.next();
				if (o instanceof FigEdgeAssociationClass) {
					figEdgeLink = (FigEdgeAssociationClass) o;
				}
			}
		}
		return figEdgeLink;
	}
}



