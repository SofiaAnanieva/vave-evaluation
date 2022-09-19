package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateMachineTopListModel extends UMLModelElementListModel2 {
	public UMLStateMachineTopListModel() {
		super("top");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getTop(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getTop(getTarget());
	}
}



