package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLAssociationLinkListModel extends UMLModelElementListModel2 {
	public UMLAssociationLinkListModel() {
		super("link");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getLinks(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isALink(o)&&Model.getFacade().getLinks(getTarget()).contains(o);
	}
}



