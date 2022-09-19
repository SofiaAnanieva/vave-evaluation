package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;


public class FigAssociationEnd extends FigEdgeModelElement {
	private FigAssociationEndAnnotation destGroup;
	private FigMultiplicity destMult;
	@SuppressWarnings("deprecation")@Deprecated public FigAssociationEnd() {
		super();
		destMult = new FigMultiplicity();
		addPathItem(destMult,new PathItemPlacement(this,destMult,100,-5,45,5));
		ArgoFigUtil.markPosition(this,100,-5,45,5,Color.green);
		destGroup = new FigAssociationEndAnnotation(this);
		addPathItem(destGroup,new PathItemPlacement(this,destGroup,100,-5,-45,5));
		ArgoFigUtil.markPosition(this,100,-5,-45,5,Color.blue);
		setBetweenNearestPoints(true);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigAssociationEnd(Object owner,Layer lay) {
		this();
		setLayer(lay);
		setOwner(owner);
		if (Model.getFacade().isAAssociationEnd(owner)) {
			addElementListener(owner);
		}
	}
	public FigAssociationEnd(Object owner,DiagramSettings settings) {
		super(owner,settings);
		destMult = new FigMultiplicity(owner,settings);
		addPathItem(destMult,new PathItemPlacement(this,destMult,100,-5,45,5));
		ArgoFigUtil.markPosition(this,100,-5,45,5,Color.green);
		destGroup = new FigAssociationEndAnnotation(this,owner,settings);
		addPathItem(destGroup,new PathItemPlacement(this,destGroup,100,-5,-45,5));
		ArgoFigUtil.markPosition(this,100,-5,-45,5,Color.blue);
		setBetweenNearestPoints(true);
		initializeNotationProvidersInternal(owner);
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		super.setOwner(owner);
		destGroup.setOwner(owner);
		destMult.setOwner(owner);
	}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME;
	}
	@SuppressWarnings("deprecation")@Override protected void initNotationProviders(Object own) {
		initializeNotationProvidersInternal(own);
	}
	@SuppressWarnings("deprecation")private void initializeNotationProvidersInternal(Object own) {
		super.initNotationProviders(own);
		destMult.initNotationProviders();
		initNotationArguments();
	}
	protected void initNotationArguments() {
	}
	@Override public void updateListeners(Object oldOwner,Object newOwner) {
		Set<Object[]>listeners = new HashSet<Object[]>();
		if (newOwner != null) {
			listeners.add(new Object[] {newOwner,new String[] {"isAbstract","remove"}});
		}
		updateElementListeners(listeners);
	}
	@Override protected void textEdited(FigText ft) {
		if (getOwner() == null) {
			return;
		}
		super.textEdited(ft);
		if (getOwner() == null) {
			return;
		}
		if (ft == destGroup.getRole()) {
			destGroup.getRole().textEdited();
		}else if (ft == destMult) {
			destMult.textEdited();
		}
	}
	@Override protected void textEditStarted(FigText ft) {
		if (ft == destGroup.getRole()) {
			destGroup.getRole().textEditStarted();
		}else if (ft == destMult) {
			destMult.textEditStarted();
		}else {
			super.textEditStarted(ft);
		}
	}
	@Override public void renderingChanged() {
		super.renderingChanged();
		destMult.renderingChanged();
		destGroup.renderingChanged();
		initNotationArguments();
	}
	@Override protected void updateStereotypeText() {
	}
	@Override public void paintClarifiers(Graphics g) {
		indicateBounds(getNameFig(),g);
		indicateBounds(destMult,g);
		indicateBounds(destGroup.getRole(),g);
		super.paintClarifiers(g);
	}
	protected void updateMultiplicity() {
		if (getOwner() != null&&destMult.getOwner() != null) {
			destMult.setText();
		}
	}
}



