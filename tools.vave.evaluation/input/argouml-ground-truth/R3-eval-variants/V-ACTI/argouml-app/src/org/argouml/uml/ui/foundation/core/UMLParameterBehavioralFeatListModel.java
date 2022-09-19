package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLParameterBehavioralFeatListModel extends UMLModelElementListModel2 {
	public UMLParameterBehavioralFeatListModel() {
		super("behavioralFeature");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			removeAllElements();
			addElement(Model.getFacade().getBehavioralFeature(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().getBehavioralFeature(getTarget()) == o;
	}
}



