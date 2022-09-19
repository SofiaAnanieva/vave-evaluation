package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateInternalTransition extends UMLModelElementListModel2 {
	public UMLStateInternalTransition() {
		super("internalTransition");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getInternalTransitions(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getInternalTransitions(getTarget()).contains(element);
	}
}



