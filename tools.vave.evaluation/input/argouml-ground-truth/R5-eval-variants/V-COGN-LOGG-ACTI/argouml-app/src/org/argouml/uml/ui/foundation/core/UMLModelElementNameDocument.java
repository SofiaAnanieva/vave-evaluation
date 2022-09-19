package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLModelElementNameDocument extends UMLPlainTextDocument {
	public UMLModelElementNameDocument() {
		super("name");
	}
	protected void setProperty(String text) {
		Model.getCoreHelper().setName(getTarget(),text);
	}
	protected String getProperty() {
		return Model.getFacade().getName(getTarget());
	}
}



