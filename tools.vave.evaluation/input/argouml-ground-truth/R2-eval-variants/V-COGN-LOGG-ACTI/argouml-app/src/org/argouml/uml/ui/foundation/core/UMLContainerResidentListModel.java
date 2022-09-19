package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLContainerResidentListModel extends UMLModelElementListModel2 {
	public UMLContainerResidentListModel() {
		super("resident");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getResidents(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return(Model.getFacade().isAComponent(o)||Model.getFacade().isAInstance(o));
	}
}



