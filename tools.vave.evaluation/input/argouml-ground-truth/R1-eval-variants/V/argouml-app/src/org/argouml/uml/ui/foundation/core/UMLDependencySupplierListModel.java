package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLDependencySupplierListModel extends UMLModelElementListModel2 {
	public UMLDependencySupplierListModel() {
		super("supplier");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getSuppliers(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAModelElement(o)&&Model.getFacade().getSuppliers(getTarget()).contains(o);
	}
}



