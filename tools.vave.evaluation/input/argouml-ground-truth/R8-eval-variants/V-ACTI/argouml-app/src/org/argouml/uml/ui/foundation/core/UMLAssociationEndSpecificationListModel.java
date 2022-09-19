package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationEndSpecificationListModel extends UMLModelElementListModel2 {
	public UMLAssociationEndSpecificationListModel() {
		super("specification");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getSpecifications(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAClassifier(o)&&Model.getFacade().getSpecifications(getTarget()).contains(o);
	}
}



