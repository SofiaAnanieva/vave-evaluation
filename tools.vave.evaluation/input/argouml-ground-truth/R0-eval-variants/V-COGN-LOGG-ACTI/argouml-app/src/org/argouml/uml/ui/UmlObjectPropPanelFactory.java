package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.uml.ui.foundation.core.PropPanelElementResidence;
import org.argouml.uml.ui.model_management.PropPanelElementImport;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanel;


class UmlObjectPropPanelFactory implements PropPanelFactory {
	public PropPanel createPropPanel(Object object) {
		if (Model.getFacade().isAExpression(object)) {
			return getExpressionPropPanel(object);
		}
		if (Model.getFacade().isAMultiplicity(object)) {
			return getMultiplicityPropPanel(object);
		}
		if (Model.getFacade().isAElementImport(object)) {
			return new PropPanelElementImport();
		}
		if (Model.getFacade().isAElementResidence(object)) {
			return new PropPanelElementResidence();
		}
		return null;
	}
	private PropPanel getExpressionPropPanel(Object object) {
		return null;
	}
	private PropPanel getMultiplicityPropPanel(Object object) {
		return null;
	}
}



