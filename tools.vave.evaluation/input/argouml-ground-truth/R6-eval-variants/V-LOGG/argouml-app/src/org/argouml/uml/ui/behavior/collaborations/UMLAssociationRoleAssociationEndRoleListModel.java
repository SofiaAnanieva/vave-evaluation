package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationRoleAssociationEndRoleListModel extends UMLModelElementListModel2 {
	public UMLAssociationRoleAssociationEndRoleListModel() {
		super("connection");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getConnections(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAAssociationEndRole(o)&&Model.getFacade().getConnections(getTarget()).contains(o);
	}
}



