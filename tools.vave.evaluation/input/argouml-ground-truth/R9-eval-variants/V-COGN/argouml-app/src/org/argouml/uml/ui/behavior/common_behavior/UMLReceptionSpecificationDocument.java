package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLReceptionSpecificationDocument extends UMLPlainTextDocument {
	public UMLReceptionSpecificationDocument() {
		super("specification");
	}
	protected void setProperty(String text) {
		if (Model.getFacade().isAReception(getTarget())) {
			Model.getCommonBehaviorHelper().setSpecification(getTarget(),text);
		}
	}
	protected String getProperty() {
		if (Model.getFacade().isAReception(getTarget())) {
			return Model.getFacade().getSpecification(getTarget());
		}
		return null;
	}
}



