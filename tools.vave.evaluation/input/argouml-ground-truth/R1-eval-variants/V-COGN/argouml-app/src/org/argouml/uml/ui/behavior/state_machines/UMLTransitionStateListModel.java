package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLTransitionStateListModel extends UMLModelElementListModel2 {
	public UMLTransitionStateListModel() {
		super("state");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getState(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getState(getTarget()) == element;
	}
}



