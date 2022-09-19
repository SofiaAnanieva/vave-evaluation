package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateEntryListModel extends UMLModelElementListModel2 {
	public UMLStateEntryListModel() {
		super("entry");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getEntry(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getEntry(getTarget());
	}
}



