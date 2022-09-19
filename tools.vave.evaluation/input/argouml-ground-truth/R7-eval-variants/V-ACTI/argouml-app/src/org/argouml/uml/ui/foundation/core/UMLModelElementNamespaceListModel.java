package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementNamespaceListModel extends UMLModelElementListModel2 {
	public UMLModelElementNamespaceListModel() {
		super("namespace");
	}
	protected void buildModelList() {
		removeAllElements();
		if (getTarget() != null) {
			addElement(Model.getFacade().getNamespace(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getNamespace(getTarget()) == element;
	}
}



