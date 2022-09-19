package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLFeatureOwnerListModel extends UMLModelElementListModel2 {
	public UMLFeatureOwnerListModel() {
		super("owner");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			removeAllElements();
			addElement(Model.getFacade().getOwner(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().getOwner(getTarget()) == o;
	}
}



