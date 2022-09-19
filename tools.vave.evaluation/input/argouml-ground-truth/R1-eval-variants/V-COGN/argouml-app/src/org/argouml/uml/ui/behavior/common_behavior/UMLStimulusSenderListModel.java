package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStimulusSenderListModel extends UMLModelElementListModel2 {
	public UMLStimulusSenderListModel() {
		super("sender");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getSender(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().getSender(getTarget()) == elem;
	}
}



