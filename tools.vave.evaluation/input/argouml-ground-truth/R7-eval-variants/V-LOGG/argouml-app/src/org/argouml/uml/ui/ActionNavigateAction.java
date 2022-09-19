package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateAction extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getAction(source);
	}
	private static final long serialVersionUID = -4136512885671684476l;
}



