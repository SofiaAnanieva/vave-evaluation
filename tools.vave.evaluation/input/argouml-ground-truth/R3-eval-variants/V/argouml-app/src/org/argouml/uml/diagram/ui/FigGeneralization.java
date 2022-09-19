package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.argouml.uml.diagram.ui.ArgoFigText;


public class FigGeneralization extends FigEdgeModelElement {
	private static final int TEXT_HEIGHT = 20;
	private static final int DISCRIMINATOR_WIDTH = 90;
	private static final long serialVersionUID = 3983170503390943894l;
	private FigText discriminator;
	private ArrowHeadTriangle endArrow;
	@SuppressWarnings("deprecation")@Deprecated public FigGeneralization() {
		discriminator = new ArgoFigText(null,new Rectangle(X0,Y0,DISCRIMINATOR_WIDTH,TEXT_HEIGHT),getSettings(),false);
		initialize();
	}
	private void initialize() {
		discriminator.setFilled(false);
		discriminator.setLineWidth(0);
		discriminator.setReturnAction(FigText.END_EDITING);
		discriminator.setTabAction(FigText.END_EDITING);
		addPathItem(discriminator,new PathItemPlacement(this,discriminator,50,-10));
		endArrow = new ArrowHeadTriangle();
		endArrow.setFillColor(FILL_COLOR);
		setDestArrowHead(endArrow);
		setBetweenNearestPoints(true);
	}
	@Deprecated public FigGeneralization(Object edge,Layer lay) {
		this();
		setLayer(lay);
		setOwner(edge);
	}
	public FigGeneralization(Object owner,DiagramSettings settings) {
		super(owner,settings);
		discriminator = new ArgoFigText(owner,new Rectangle(X0,Y0,DISCRIMINATOR_WIDTH,TEXT_HEIGHT),settings,false);
		initialize();
		fixup(owner);
		addListener(owner);
	}
	@Override protected boolean canEdit(Fig f) {
		return false;
	}
	@Override protected void modelChanged(PropertyChangeEvent e) {
		super.modelChanged(e);
		if (e instanceof AttributeChangeEvent&&"discriminator".equals(e.getPropertyName())) {
			updateDiscriminatorText();
		}
	}
	@Override protected void updateListeners(Object oldOwner,Object newOwner) {
		if (oldOwner != null) {
			removeElementListener(oldOwner);
		}
		if (newOwner != null) {
			addListener(newOwner);
		}
	}
	private void addListener(Object owner) {
		addElementListener(owner,new String[] {"remove","discriminator"});
	}
	public void updateDiscriminatorText() {
		Object generalization = getOwner();
		if (generalization == null) {
			return;
		}
		String disc = (String) Model.getFacade().getDiscriminator(generalization);
		if (disc == null) {
			disc = "";
		}
		discriminator.setFont(getSettings().getFont(Font.PLAIN));
		discriminator.setText(disc);
	}
	@Override public void paint(Graphics g) {
		endArrow.setLineColor(getLineColor());
		super.paint(g);
	}
	@SuppressWarnings("deprecation")@Override public void setOwner(Object own) {
		super.setOwner(own);
		fixup(own);
	}
	private void fixup(Object owner) {
		if (Model.getFacade().isAGeneralization(owner)) {
			Object subType = Model.getFacade().getSpecific(owner);
			Object superType = Model.getFacade().getGeneral(owner);
			if (subType == null||superType == null) {
				removeFromDiagram();
				return;
			}
			updateDiscriminatorText();
		}else if (owner != null) {
			throw new IllegalStateException("FigGeneralization has an illegal owner of " + owner.getClass().getName());
		}
	}
}



