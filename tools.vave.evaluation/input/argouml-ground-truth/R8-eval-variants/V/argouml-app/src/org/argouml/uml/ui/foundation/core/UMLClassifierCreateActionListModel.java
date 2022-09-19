package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLClassifierCreateActionListModel extends UMLModelElementListModel2 {
	public UMLClassifierCreateActionListModel() {
		super("createAction");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getCreateActions(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getCreateActions(getTarget()).contains(element);
	}
}



