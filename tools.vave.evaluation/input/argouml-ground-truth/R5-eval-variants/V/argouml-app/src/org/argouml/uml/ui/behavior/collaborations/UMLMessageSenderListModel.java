package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLMessageSenderListModel extends UMLModelElementListModel2 {
	public UMLMessageSenderListModel() {
		super("sender");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getSender(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().getSender(getTarget()) == elem;
	}
}



