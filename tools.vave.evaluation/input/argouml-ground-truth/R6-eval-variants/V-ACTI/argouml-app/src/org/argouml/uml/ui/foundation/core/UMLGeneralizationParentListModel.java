package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLGeneralizationParentListModel extends UMLModelElementListModel2 {
	public UMLGeneralizationParentListModel() {
		super("parent");
	}
	protected void buildModelList() {
		if (getTarget() == null) {
			return;
		}
		removeAllElements();
		addElement(Model.getFacade().getGeneral(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return(Model.getFacade().getGeneral(getTarget()) == o);
	}
}



