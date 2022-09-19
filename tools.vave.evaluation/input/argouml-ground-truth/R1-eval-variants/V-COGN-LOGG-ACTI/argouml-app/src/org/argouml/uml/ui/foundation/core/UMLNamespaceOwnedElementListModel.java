package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLNamespaceOwnedElementListModel extends UMLModelElementListModel2 {
	public UMLNamespaceOwnedElementListModel() {
		super("ownedElement");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getOwnedElements(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getOwnedElements(getTarget()).contains(element);
	}
}



