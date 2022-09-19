package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateMachineContextListModel extends UMLModelElementListModel2 {
	public UMLStateMachineContextListModel() {
		super("context");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getContext(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getContext(getTarget());
	}
}



