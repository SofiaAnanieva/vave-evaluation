package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateAssociation extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getAssociation(source);
	}
}



