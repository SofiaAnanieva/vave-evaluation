package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathItemPlacement;


public class FigDependency extends FigEdgeModelElement {
	private static final long serialVersionUID = -1779182458484724448l;
	private FigTextGroup middleGroup;
	@SuppressWarnings("deprecation")@Deprecated public FigDependency() {
		super();
		middleGroup = new FigTextGroup();
		constructFigs();
	}
	private void constructFigs() {
		middleGroup.addFig(getNameFig());
		middleGroup.addFig(getStereotypeFig());
		addPathItem(middleGroup,new PathItemPlacement(this,middleGroup,50,25));
		setDestArrowHead(createEndArrow());
		setBetweenNearestPoints(true);
		getFig().setDashed(true);
	}
	protected ArrowHead createEndArrow() {
		return new ArrowHeadGreater();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigDependency(Object dependency) {
		this();
		setOwner(dependency);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigDependency(Object dependency,Layer lay) {
		this();
		setOwner(dependency);
		setLayer(lay);
	}
	public FigDependency(Object owner,DiagramSettings settings) {
		super(owner,settings);
		middleGroup = new FigTextGroup(owner,settings);
		constructFigs();
	}
	@Override public void setFig(Fig f) {
		super.setFig(f);
		getFig().setDashed(true);
	}
	@Override protected boolean canEdit(Fig f) {
		return false;
	}
	public void setLineColor(Color color) {
		ArrowHead arrow = getDestArrowHead();
		if (arrow != null) {
			arrow.setLineColor(getLineColor());
		}
	}
	@Override protected void updateNameText() {
		super.updateNameText();
		middleGroup.calcBounds();
	}
	@Override protected void updateStereotypeText() {
		super.updateStereotypeText();
		middleGroup.calcBounds();
	}
}



