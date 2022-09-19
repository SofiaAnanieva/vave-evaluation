package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInteractionContextListModel extends UMLModelElementListModel2 {
	public UMLInteractionContextListModel() {
		super("context");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getContext(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().isACollaboration(elem)&&Model.getFacade().getInteractions(elem).contains(getTarget());
	}
}



