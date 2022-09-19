package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementSupplierDependencyListModel extends UMLModelElementListModel2 {
	public UMLModelElementSupplierDependencyListModel() {
		super("supplierDependency",Model.getMetaTypes().getDependency(),true);
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getSupplierDependencies(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isADependency(o)&&Model.getFacade().getSupplierDependencies(getTarget()).contains(o);
	}
}



