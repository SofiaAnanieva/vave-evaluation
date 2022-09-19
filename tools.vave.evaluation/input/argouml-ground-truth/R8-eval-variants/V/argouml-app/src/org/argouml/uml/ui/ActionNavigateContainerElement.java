package org.argouml.uml.ui;

import org.argouml.model.Model;


public class ActionNavigateContainerElement extends AbstractActionNavigate {
	protected Object navigateTo(Object source) {
		return Model.getFacade().getModelElementContainer(source);
	}
}



