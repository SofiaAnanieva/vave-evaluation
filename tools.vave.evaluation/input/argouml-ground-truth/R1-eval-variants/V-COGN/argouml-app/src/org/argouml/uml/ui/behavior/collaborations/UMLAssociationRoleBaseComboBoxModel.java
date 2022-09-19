package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLAssociationRoleBaseComboBoxModel extends UMLComboBoxModel2 {
	private Collection others = new ArrayList();
	public UMLAssociationRoleBaseComboBoxModel() {
		super("base",true);
	}
	@Override protected void buildModelList() {
		removeAllElements();
		Object ar = getTarget();
		Object base = Model.getFacade().getBase(ar);
		if (base != null) {
			addElement(base);
		}
	}
	@Override protected Object getSelectedModelElement() {
		Object ar = getTarget();
		if (Model.getFacade().isAAssociationRole(ar)) {
			Object base = Model.getFacade().getBase(ar);
			if (base != null) {
				return base;
			}
		}
		return null;
	}
	@Override protected boolean isValidElement(Object element) {
		Object ar = getTarget();
		if (Model.getFacade().isAAssociationRole(ar)) {
			Object base = Model.getFacade().getBase(ar);
			if (element == base) {
				return true;
			}
		}
		return false;
	}
	@Override protected void addOtherModelEventListeners(Object newTarget) {
		super.addOtherModelEventListeners(newTarget);
		Collection connections = Model.getFacade().getConnections(newTarget);
		Collection types = new ArrayList();
		for (Object conn:connections) {
			types.add(Model.getFacade().getType(conn));
		}
		for (Object classifierRole:types) {
			others.addAll(Model.getFacade().getBases(classifierRole));
		}
		for (Object classifier:others) {
			Model.getPump().addModelEventListener(this,classifier,"feature");
		}
	}
	@Override protected void removeOtherModelEventListeners(Object oldTarget) {
		super.removeOtherModelEventListeners(oldTarget);
		for (Object classifier:others) {
			Model.getPump().removeModelEventListener(this,classifier,"feature");
		}
		others.clear();
	}
}



