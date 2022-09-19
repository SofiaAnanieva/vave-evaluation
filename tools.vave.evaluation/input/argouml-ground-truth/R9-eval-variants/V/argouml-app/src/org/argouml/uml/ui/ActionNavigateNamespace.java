package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateNamespace extends AbstractActionNavigate {
	protected Object navigateTo(Object elem) {
		return Model.getFacade().getNamespace(elem);
	}
}



