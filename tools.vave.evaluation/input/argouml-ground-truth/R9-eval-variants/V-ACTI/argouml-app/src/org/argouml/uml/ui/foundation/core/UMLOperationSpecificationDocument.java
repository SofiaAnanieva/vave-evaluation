package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLOperationSpecificationDocument extends UMLPlainTextDocument {
	private static final long serialVersionUID = -152721992761681537l;
	public UMLOperationSpecificationDocument() {
		super("specification");
	}
	protected void setProperty(String text) {
		if (Model.getFacade().isAOperation(getTarget())||Model.getFacade().isAReception(getTarget())) {
			Model.getCoreHelper().setSpecification(getTarget(),text);
		}
	}
	protected String getProperty() {
		if (Model.getFacade().isAOperation(getTarget())||Model.getFacade().isAReception(getTarget())) {
			return Model.getFacade().getSpecification(getTarget());
		}
		return null;
	}
}



