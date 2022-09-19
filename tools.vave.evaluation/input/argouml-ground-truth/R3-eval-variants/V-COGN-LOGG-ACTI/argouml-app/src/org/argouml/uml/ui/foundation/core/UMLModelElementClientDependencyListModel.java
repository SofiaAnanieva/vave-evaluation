package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementClientDependencyListModel extends UMLModelElementListModel2 {
	public UMLModelElementClientDependencyListModel() {
		super("clientDependency",Model.getMetaTypes().getDependency());
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getClientDependencies(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isADependency(o)&&Model.getFacade().getClientDependencies(getTarget()).contains(o);
	}
}



