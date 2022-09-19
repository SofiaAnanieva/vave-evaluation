package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateExitListModel extends UMLModelElementListModel2 {
	public UMLStateExitListModel() {
		super("exit");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getExit(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getExit(getTarget());
	}
}



