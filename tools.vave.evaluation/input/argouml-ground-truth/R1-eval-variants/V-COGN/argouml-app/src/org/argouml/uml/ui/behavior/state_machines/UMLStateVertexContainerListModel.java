package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateVertexContainerListModel extends UMLModelElementListModel2 {
	public UMLStateVertexContainerListModel() {
		super("container");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getContainer(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getContainer(getTarget()) == element;
	}
}



