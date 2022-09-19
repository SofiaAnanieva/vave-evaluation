package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationAssociationRoleListModel extends UMLModelElementListModel2 {
	public UMLAssociationAssociationRoleListModel() {
		super("associationRole");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getAssociationRoles(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAAssociationRole(o)&&Model.getFacade().getAssociationRoles(getTarget()).contains(o);
	}
}



