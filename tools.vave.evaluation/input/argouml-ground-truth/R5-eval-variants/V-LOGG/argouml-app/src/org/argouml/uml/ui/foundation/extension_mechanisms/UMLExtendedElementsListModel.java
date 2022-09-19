package org.argouml.uml.ui.foundation.extension_mechanisms;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


class UMLExtendedElementsListModel extends UMLModelElementListModel2 {
	public UMLExtendedElementsListModel() {
		super("extendedElement");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getExtendedElements(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAModelElement(element)&&Model.getFacade().getExtendedElements(getTarget()).contains(element);
	}
}



