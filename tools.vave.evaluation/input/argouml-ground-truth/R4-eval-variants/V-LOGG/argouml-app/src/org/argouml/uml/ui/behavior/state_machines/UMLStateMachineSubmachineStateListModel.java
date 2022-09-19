package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateMachineSubmachineStateListModel extends UMLModelElementListModel2 {
	public UMLStateMachineSubmachineStateListModel() {
		super("submachineState");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getSubmachineStates(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getSubmachineStates(getTarget()).contains(element);
	}
}



