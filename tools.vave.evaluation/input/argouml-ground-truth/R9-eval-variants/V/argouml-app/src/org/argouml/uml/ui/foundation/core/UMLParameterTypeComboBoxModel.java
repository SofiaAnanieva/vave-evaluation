package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;


public class UMLParameterTypeComboBoxModel extends UMLStructuralFeatureTypeComboBoxModel {
	public UMLParameterTypeComboBoxModel() {
		super();
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getType(getTarget());
		}
		return null;
	}
}



