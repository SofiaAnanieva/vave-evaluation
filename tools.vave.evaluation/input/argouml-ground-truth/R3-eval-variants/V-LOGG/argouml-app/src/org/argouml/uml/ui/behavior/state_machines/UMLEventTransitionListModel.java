package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLEventTransitionListModel extends UMLModelElementListModel2 {
	public UMLEventTransitionListModel() {
		super("transition");
	}
	protected void buildModelList() {
		removeAllElements();
		addAll(Model.getFacade().getTransitions(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getTransitions(getTarget()).contains(element);
	}
}



