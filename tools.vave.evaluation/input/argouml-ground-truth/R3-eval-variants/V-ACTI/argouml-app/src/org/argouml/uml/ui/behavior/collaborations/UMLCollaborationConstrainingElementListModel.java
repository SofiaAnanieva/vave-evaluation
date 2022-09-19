package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLCollaborationConstrainingElementListModel extends UMLModelElementListModel2 {
	public UMLCollaborationConstrainingElementListModel() {
		super("constrainingElement");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getConstrainingElements(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return(Model.getFacade().getConstrainingElements(getTarget()).contains(elem));
	}
}



