package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementConstraintListModel extends UMLModelElementListModel2 {
	public UMLModelElementConstraintListModel() {
		super("constraint");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getConstraints(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAConstraint(o)&&Model.getFacade().getConstraints(getTarget()).contains(o);
	}
}



