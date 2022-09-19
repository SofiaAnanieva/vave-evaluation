package org.argouml.uml.diagram.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import org.apache.log4j.Logger;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeAssociation;
import org.argouml.uml.diagram.ui.FigClassAssociationClass;


public class FigEdgeAssociationClass extends FigEdgeModelElement implements VetoableChangeListener,DelayedVChangeListener,MouseListener,KeyListener,PropertyChangeListener {
	private static final long serialVersionUID = 4627163341288968877l;
	private static final Logger LOG = Logger.getLogger(FigEdgeAssociationClass.class);
	@SuppressWarnings("deprecation")@Deprecated public FigEdgeAssociationClass() {
		setBetweenNearestPoints(true);
		((FigPoly) getFig()).setRectilinear(false);
		setDashed(true);
	}
	@Deprecated public FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,FigAssociationClass ownerFig) {
		this();
		constructFigs(classBoxFig,ownerFig);
	}
	private void constructFigs(FigClassAssociationClass classBoxFig,Fig ownerFig) {
		LOG.info("FigEdgeAssociationClass constructor");
		if (classBoxFig == null) {
			throw new IllegalArgumentException("No class box found while " + "creating FigEdgeAssociationClass");
		}
		if (ownerFig == null) {
			throw new IllegalArgumentException("No association edge found " + "while creating FigEdgeAssociationClass");
		}
		setDestFigNode(classBoxFig);
		setDestPortFig(classBoxFig);
		final FigNode port;
		if (ownerFig instanceof FigEdgeModelElement) {
			((FigEdgeModelElement) ownerFig).makeEdgePort();
			port = ((FigEdgeModelElement) ownerFig).getEdgePort();
		}else {
			port = (FigNode) ownerFig;
		}
		setSourcePortFig(port);
		setSourceFigNode(port);
		computeRoute();
	}
	FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,FigAssociationClass ownerFig,DiagramSettings settings) {
			super(ownerFig.getOwner(),settings);
			constructFigs(classBoxFig,ownerFig);
		}
	public FigEdgeAssociationClass(FigClassAssociationClass classBoxFig,FigNodeAssociation ownerFig,DiagramSettings settings) {
		super(ownerFig.getOwner(),settings);
		constructFigs(classBoxFig,ownerFig);
	}
	@Override public void setFig(Fig f) {
		super.setFig(f);
		getFig().setDashed(true);
	}
	@Override protected boolean canEdit(Fig f) {
		return false;
	}
	@Override protected void modelChanged(PropertyChangeEvent e) {
	}
	@Override protected Fig getRemoveDelegate() {
		FigNode node = getDestFigNode();
		if (!(node instanceof FigEdgePort||node instanceof FigNodeAssociation)) {
			node = getSourceFigNode();
		}
		if (!(node instanceof FigEdgePort||node instanceof FigNodeAssociation)) {
			LOG.warn("The is no FigEdgePort attached" + " to the association class link");
			return null;
		}
		final Fig delegate;
		if (node instanceof FigEdgePort) {
			delegate = node.getGroup();
		}else {
			delegate = node;
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("Delegating remove to " + delegate.getClass().getName());
		}
		return delegate;
	}
	@Override public void setDestFigNode(FigNode fn) {
		if (!(fn instanceof FigClassAssociationClass)) {
			throw new IllegalArgumentException("The dest of an association class dashed link can " + "only be a FigClassAssociationClass");
		}
		super.setDestFigNode(fn);
	}
	@Override public void setSourceFigNode(FigNode fn) {
		if (!(fn instanceof FigEdgePort||fn instanceof FigNodeAssociation)) {
			throw new IllegalArgumentException("The source of an association class dashed link can " + "only be a FigEdgePort");
		}
		super.setSourceFigNode(fn);
	}
}



