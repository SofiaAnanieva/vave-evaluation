package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationRoleMessageListModel extends UMLModelElementListModel2 {
	public UMLAssociationRoleMessageListModel() {
		super("message");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getMessages(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAMessage(o)&&Model.getFacade().getMessages(getTarget()).contains(o);
	}
}



