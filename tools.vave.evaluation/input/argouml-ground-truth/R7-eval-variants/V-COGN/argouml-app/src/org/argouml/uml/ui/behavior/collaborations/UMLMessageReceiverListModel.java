package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLMessageReceiverListModel extends UMLModelElementListModel2 {
	public UMLMessageReceiverListModel() {
		super("receiver");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getReceiver(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getReceiver(getTarget()) == element;
	}
}



