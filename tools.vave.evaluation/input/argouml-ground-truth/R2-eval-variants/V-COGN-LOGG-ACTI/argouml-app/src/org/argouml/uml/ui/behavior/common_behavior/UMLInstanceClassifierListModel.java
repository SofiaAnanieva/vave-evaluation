package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInstanceClassifierListModel extends UMLModelElementListModel2 {
	public UMLInstanceClassifierListModel() {
		super("classifier");
	}
	protected void buildModelList() {
		if (getTarget() != null)setAllElements(Model.getFacade().getClassifiers(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAClassifier(o)&&Model.getFacade().getClassifiers(getTarget()).contains(o);
	}
}



