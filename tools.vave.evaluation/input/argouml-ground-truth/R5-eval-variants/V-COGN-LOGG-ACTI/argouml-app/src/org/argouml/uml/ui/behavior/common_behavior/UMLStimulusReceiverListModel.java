package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStimulusReceiverListModel extends UMLModelElementListModel2 {
	public UMLStimulusReceiverListModel() {
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



