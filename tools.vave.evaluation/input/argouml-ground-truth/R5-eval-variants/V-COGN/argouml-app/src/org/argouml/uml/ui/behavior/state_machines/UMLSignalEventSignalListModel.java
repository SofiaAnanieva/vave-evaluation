package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


class UMLSignalEventSignalListModel extends UMLModelElementListModel2 {
	public UMLSignalEventSignalListModel() {
		super("signal");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getSignal(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getSignal(getTarget());
	}
}



