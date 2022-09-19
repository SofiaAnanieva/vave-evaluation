package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateDoActivityListModel extends UMLModelElementListModel2 {
	public UMLStateDoActivityListModel() {
		super("doActivity");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getDoActivity(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getDoActivity(getTarget());
	}
}



