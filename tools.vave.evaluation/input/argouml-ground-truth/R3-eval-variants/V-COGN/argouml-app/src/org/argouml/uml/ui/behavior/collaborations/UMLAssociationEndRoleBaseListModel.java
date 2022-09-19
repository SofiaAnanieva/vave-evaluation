package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationEndRoleBaseListModel extends UMLModelElementListModel2 {
	public UMLAssociationEndRoleBaseListModel() {
		super("base");
	}
	protected void buildModelList() {
		removeAllElements();
		if (getTarget() != null&&Model.getFacade().getBase(getTarget()) != null) {
			addElement(Model.getFacade().getBase(getTarget()));
		}
	}
	protected boolean isValidElement(Object base) {
		if (!Model.getFacade().isAAssociationEnd(base)) {
			return false;
		}
		Object assocEndRole = getTarget();
		Object assocRole = Model.getFacade().getAssociation(assocEndRole);
		return Model.getFacade().getConnections(Model.getFacade().getBase(assocRole)).contains(base);
	}
}



