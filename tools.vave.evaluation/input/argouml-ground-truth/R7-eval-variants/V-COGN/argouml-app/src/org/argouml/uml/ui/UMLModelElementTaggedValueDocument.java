package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLModelElementTaggedValueDocument extends UMLPlainTextDocument {
	public UMLModelElementTaggedValueDocument(String taggedValue) {
		super(taggedValue);
	}
	protected void setProperty(String text) {
		if (getTarget() != null) {
			Model.getCoreHelper().setTaggedValue(getTarget(),getEventName(),text);
		}
	}
	protected String getProperty() {
		String eventName = getEventName();
		if (Model.getFacade().isAModelElement(getTarget())) {
			Object taggedValue = Model.getFacade().getTaggedValue(getTarget(),eventName);
			if (taggedValue != null) {
				return Model.getFacade().getValueOfTag(taggedValue);
			}
		}
		return"";
	}
}



