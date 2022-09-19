package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLCollaborationInteractionListModel extends UMLModelElementListModel2 {
	public UMLCollaborationInteractionListModel() {
		super("interaction");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getInteractions(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().isAInteraction(elem)&&Model.getFacade().getContext(elem) == getTarget();
	}
}



