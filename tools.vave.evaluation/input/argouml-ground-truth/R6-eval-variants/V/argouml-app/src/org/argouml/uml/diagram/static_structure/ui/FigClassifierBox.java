package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Action;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.FigCompartmentBox;


public abstract class FigClassifierBox extends FigCompartmentBox implements OperationsCompartmentContainer {
	private FigOperationsCompartment operationsFig;
	protected Fig borderFig;
	@SuppressWarnings("deprecation")@Deprecated FigClassifierBox() {
		super();
		Rectangle bounds = getDefaultBounds();
		operationsFig = new FigOperationsCompartment(bounds.x,bounds.y,bounds.width,bounds.height);
		constructFigs();
	}
	private void constructFigs() {
		getStereotypeFig().setFilled(true);
		getStereotypeFig().setLineWidth(LINE_WIDTH);
		getStereotypeFig().setHeight(STEREOHEIGHT + 1);
		borderFig = new FigEmptyRect(X0,Y0,0,0);
		borderFig.setLineWidth(LINE_WIDTH);
		borderFig.setLineColor(LINE_COLOR);
		getBigPort().setLineWidth(0);
		getBigPort().setFillColor(FILL_COLOR);
	}
	private Rectangle getDefaultBounds() {
		Rectangle bounds = new Rectangle(DEFAULT_COMPARTMENT_BOUNDS);
		return bounds;
	}
	public FigClassifierBox(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		operationsFig = new FigOperationsCompartment(owner,getDefaultBounds(),getSettings());
		constructFigs();
	}
	public Object clone() {
		FigClassifierBox figClone = (FigClassifierBox) super.clone();
		Iterator thisIter = this.getFigs().iterator();
		while (thisIter.hasNext()) {
			Fig thisFig = (Fig) thisIter.next();
			if (thisFig == operationsFig) {
				figClone.operationsFig = (FigOperationsCompartment) thisFig;
				return figClone;
			}
		}
		return figClone;
	}
	protected void updateOperations() {
		if (!isOperationsVisible()) {
			return;
		}
		operationsFig.populate();
		setBounds(getBounds());
		damage();
	}
	public void renderingChanged() {
		super.renderingChanged();
		updateOperations();
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("generalization")&&Model.getFacade().isAGeneralization(event.getOldValue())) {
			return;
		}else if (event.getPropertyName().equals("association")&&Model.getFacade().isAAssociationEnd(event.getOldValue())) {
			return;
		}else if (event.getPropertyName().equals("supplierDependency")&&Model.getFacade().isAUsage(event.getOldValue())) {
			return;
		}else if (event.getPropertyName().equals("clientDependency")&&Model.getFacade().isAAbstraction(event.getOldValue())) {
			return;
		}
		super.propertyChange(event);
	}
	protected void updateLayout(UmlChangeEvent event) {
		super.updateLayout(event);
		if (event instanceof AssociationChangeEvent&&getOwner().equals(event.getSource())) {
			Object o = null;
			if (event instanceof AddAssociationEvent) {
				o = event.getNewValue();
			}else if (event instanceof RemoveAssociationEvent) {
				o = event.getOldValue();
			}
			if (Model.getFacade().isAOperation(o)||Model.getFacade().isAReception(o)) {
				updateOperations();
			}
		}
	}
	protected FigOperationsCompartment getOperationsFig() {
		return operationsFig;
	}
	public Rectangle getOperationsBounds() {
		return operationsFig.getBounds();
	}
	public boolean isOperationsVisible() {
		return operationsFig != null&&operationsFig.isVisible();
	}
	public void setOperationsVisible(boolean isVisible) {
		setCompartmentVisible(operationsFig,isVisible);
	}
	public void translate(int dx,int dy) {
		super.translate(dx,dy);
		Editor ce = Globals.curEditor();
		if (ce != null) {
			Selection sel = ce.getSelectionManager().findSelectionFor(this);
			if (sel instanceof SelectionClass) {
				((SelectionClass) sel).hideButtons();
			}
		}
	}
	public Vector getPopUpActions(MouseEvent me) {
		Vector popUpActions = super.getPopUpActions(me);
		ArgoJMenu addMenu = buildAddMenu();
		popUpActions.add(popUpActions.size() - getPopupAddOffset(),addMenu);
		popUpActions.add(popUpActions.size() - getPopupAddOffset(),buildModifierPopUp());
		popUpActions.add(popUpActions.size() - getPopupAddOffset(),buildVisibilityPopUp());
		return popUpActions;
	}
	protected ArgoJMenu buildShowPopUp() {
		ArgoJMenu showMenu = super.buildShowPopUp();
		Iterator i = ActionCompartmentDisplay.getActions().iterator();
		while (i.hasNext()) {
			showMenu.add((Action) i.next());
		}
		return showMenu;
	}
	protected ArgoJMenu buildAddMenu() {
		ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
		Action addOperation = new ActionAddOperation();
		addOperation.setEnabled(isSingleTarget());
		addMenu.insert(addOperation,0);
		addMenu.add(new ActionAddNote());
		addMenu.add(ActionEdgesDisplay.getShowEdges());
		addMenu.add(ActionEdgesDisplay.getHideEdges());
		return addMenu;
	}
	public String classNameAndBounds() {
		return super.classNameAndBounds() + "operationsVisible=" + isOperationsVisible() + ";";
	}
	protected Object buildModifierPopUp() {
		return buildModifierPopUp(ABSTRACT|LEAF|ROOT);
	}
}



