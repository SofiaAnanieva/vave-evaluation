package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateTransition extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getTransition(source);
	}
}



