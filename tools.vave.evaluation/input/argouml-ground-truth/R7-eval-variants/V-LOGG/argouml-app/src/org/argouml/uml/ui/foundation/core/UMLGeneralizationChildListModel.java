package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLGeneralizationChildListModel extends UMLModelElementListModel2 {
	public UMLGeneralizationChildListModel() {
		super("child");
	}
	protected void buildModelList() {
		if (getTarget() == null) {
			return;
		}
		removeAllElements();
		addElement(Model.getFacade().getSpecific(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return(Model.getFacade().getSpecific(getTarget()) == o);
	}
}



