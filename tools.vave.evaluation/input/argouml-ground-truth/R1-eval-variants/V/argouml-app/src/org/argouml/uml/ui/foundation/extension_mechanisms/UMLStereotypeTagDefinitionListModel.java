package org.argouml.uml.ui.foundation.extension_mechanisms;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


class UMLStereotypeTagDefinitionListModel extends UMLModelElementListModel2 {
	public UMLStereotypeTagDefinitionListModel() {
		super("definedTag");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getTagDefinitions(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isATagDefinition(element)&&Model.getFacade().getTagDefinitions(getTarget()).contains(element);
	}
}



