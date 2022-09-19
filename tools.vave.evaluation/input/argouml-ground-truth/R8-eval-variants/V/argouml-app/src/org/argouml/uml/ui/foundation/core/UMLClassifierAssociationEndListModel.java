package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLClassifierAssociationEndListModel extends UMLModelElementListModel2 {
	public UMLClassifierAssociationEndListModel() {
		super("association",Model.getMetaTypes().getAssociation());
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getAssociationEnds(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getAssociationEnds(getTarget()).contains(element);
	}
}



