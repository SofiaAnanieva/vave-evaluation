package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLConstraintConstrainedElementListModel extends UMLModelElementListModel2 {
	public UMLConstraintConstrainedElementListModel() {
		super("constrainedElement");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getConstrainedElements(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAModelElement(element)&&Model.getFacade().getConstrainedElements(getTarget()).contains(element);
	}
}



