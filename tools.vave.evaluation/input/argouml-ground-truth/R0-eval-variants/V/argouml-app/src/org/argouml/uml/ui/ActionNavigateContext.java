package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateContext extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getContext(source);
	}
}



