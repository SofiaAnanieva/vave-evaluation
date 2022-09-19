package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLEventParameterListModel extends UMLModelElementListModel2 {
	public UMLEventParameterListModel() {
		super("parameter");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getParameters(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getParameters(getTarget()).contains(element);
	}
}



