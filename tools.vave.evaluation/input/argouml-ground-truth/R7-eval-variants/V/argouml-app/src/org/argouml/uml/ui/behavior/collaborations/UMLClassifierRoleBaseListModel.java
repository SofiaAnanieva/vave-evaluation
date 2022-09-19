package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLClassifierRoleBaseListModel extends UMLModelElementListModel2 {
	public UMLClassifierRoleBaseListModel() {
		super("base");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getBases(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().isAClassifier(elem)&&Model.getFacade().getBases(getTarget()).contains(elem);
	}
}



