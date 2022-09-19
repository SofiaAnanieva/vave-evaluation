package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLTransitionStatemachineListModel extends UMLModelElementListModel2 {
	public UMLTransitionStatemachineListModel() {
		super("statemachine");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getStateMachine(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getStateMachine(getTarget()) == element;
	}
}



