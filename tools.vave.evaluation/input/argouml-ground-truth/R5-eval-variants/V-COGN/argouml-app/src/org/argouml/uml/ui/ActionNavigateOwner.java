package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateOwner extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getOwner(source);
	}
}



