package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.argouml.uml.diagram.ui.FigCompartmentBox;


public class FigClassifierBoxWithAttributes extends FigClassifierBox implements AttributesCompartmentContainer {
	private static final Logger LOG = Logger.getLogger(FigClassifierBoxWithAttributes.class);
	private FigAttributesCompartment attributesFigCompartment;
	@SuppressWarnings("deprecation")@Deprecated public FigClassifierBoxWithAttributes() {
		super();
	}
	public FigClassifierBoxWithAttributes(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		attributesFigCompartment = new FigAttributesCompartment(owner,DEFAULT_COMPARTMENT_BOUNDS,settings);
	}
	protected FigAttributesCompartment getAttributesFig() {
		return attributesFigCompartment;
	}
	public Rectangle getAttributesBounds() {
		return attributesFigCompartment.getBounds();
	}
	public boolean isAttributesVisible() {
		return attributesFigCompartment != null&&attributesFigCompartment.isVisible();
	}
	@Override protected ArgoJMenu buildAddMenu() {
		ArgoJMenu addMenu = super.buildAddMenu();
		Action addAttribute = new ActionAddAttribute();
		addAttribute.setEnabled(isSingleTarget());
		addMenu.insert(addAttribute,0);
		return addMenu;
	}
	@Override public String classNameAndBounds() {
		return super.classNameAndBounds() + "attributesVisible=" + isAttributesVisible() + ";";
	}
	@Override protected void updateListeners(Object oldOwner,Object newOwner) {
		Set<Object[]>listeners = new HashSet<Object[]>();
		if (newOwner != null) {
			listeners.add(new Object[] {newOwner,null});
			for (Object stereotype:Model.getFacade().getStereotypes(newOwner)) {
				listeners.add(new Object[] {stereotype,null});
			}
			for (Object feat:Model.getFacade().getFeatures(newOwner)) {
				listeners.add(new Object[] {feat,null});
				for (Object stereotype:Model.getFacade().getStereotypes(feat)) {
					listeners.add(new Object[] {stereotype,null});
				}
				if (Model.getFacade().isAOperation(feat)) {
					for (Object param:Model.getFacade().getParameters(feat)) {
						listeners.add(new Object[] {param,null});
					}
				}
			}
		}
		updateElementListeners(listeners);
	}
	@Override public void renderingChanged() {
		super.renderingChanged();
		if (getOwner() != null) {
			updateAttributes();
		}
	}
	@Override protected void updateLayout(UmlChangeEvent event) {
		super.updateLayout(event);
		if (event instanceof AttributeChangeEvent) {
			Object source = event.getSource();
			if (Model.getFacade().isAAttribute(source)) {
				updateAttributes();
			}
		}else if (event instanceof AssociationChangeEvent&&getOwner().equals(event.getSource())) {
			Object o = null;
			if (event instanceof AddAssociationEvent) {
				o = event.getNewValue();
			}else if (event instanceof RemoveAssociationEvent) {
				o = event.getOldValue();
			}
			if (Model.getFacade().isAAttribute(o)) {
				updateAttributes();
			}
		}
	}
	protected void updateAttributes() {
		if (!isAttributesVisible()) {
			return;
		}
		attributesFigCompartment.populate();
		setBounds(getBounds());
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		attributesFigCompartment.setOwner(owner);
		super.setOwner(owner);
	}
	public void setAttributesVisible(boolean isVisible) {
		setCompartmentVisible(attributesFigCompartment,isVisible);
	}
	@Override public Dimension getMinimumSize() {
		Dimension aSize = getNameFig().getMinimumSize();
		aSize.height += NAME_V_PADDING * 2;
		aSize.height = Math.max(NAME_FIG_HEIGHT,aSize.height);
		aSize = addChildDimensions(aSize,getStereotypeFig());
		aSize = addChildDimensions(aSize,getAttributesFig());
		aSize = addChildDimensions(aSize,getOperationsFig());
		aSize.width = Math.max(WIDTH,aSize.width);
		return aSize;
	}
	@Override protected void setStandardBounds(final int x,final int y,final int width,final int height) {
		Rectangle oldBounds = getBounds();
		int w = Math.max(width,getMinimumSize().width);
		int h = Math.max(height,getMinimumSize().height);
		getBigPort().setBounds(x,y,w,h);
		if (borderFig != null) {
			borderFig.setBounds(x,y,w,h);
		}
		final int whitespace = h - getMinimumSize().height;
		int currentHeight = 0;
		if (getStereotypeFig().isVisible()) {
			int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
			getStereotypeFig().setBounds(x,y,w,stereotypeHeight);
			currentHeight += stereotypeHeight;
		}
		int nameHeight = getNameFig().getMinimumSize().height;
		getNameFig().setBounds(x,y + currentHeight,w,nameHeight);
		currentHeight += nameHeight;
		if (isAttributesVisible()) {
			int attributesHeight = attributesFigCompartment.getMinimumSize().height;
			if (isOperationsVisible()) {
				attributesHeight += whitespace / 2;
			}
			attributesFigCompartment.setBounds(x,y + currentHeight,w,attributesHeight);
			currentHeight += attributesHeight;
		}
		if (isOperationsVisible()) {
			int operationsY = y + currentHeight;
			int operationsHeight = (h + y) - operationsY - LINE_WIDTH;
			if (operationsHeight < getOperationsFig().getMinimumSize().height) {
				operationsHeight = getOperationsFig().getMinimumSize().height;
			}
			getOperationsFig().setBounds(x,operationsY,w,operationsHeight);
		}
		calcBounds();
		updateEdges();
		LOG.debug("Bounds change : old - " + oldBounds + ", new - " + getBounds());
		firePropChange("bounds",oldBounds,getBounds());
	}
}



