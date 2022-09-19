package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLMessageInteractionListModel extends UMLModelElementListModel2 {
	public UMLMessageInteractionListModel() {
		super("interaction");
	}
	protected void buildModelList() {
		if (Model.getFacade().isAMessage(getTarget())) {
			removeAllElements();
			addElement(Model.getFacade().getInteraction(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAInteraction(element)&&Model.getFacade().getInteraction(getTarget()) == element;
	}
}



