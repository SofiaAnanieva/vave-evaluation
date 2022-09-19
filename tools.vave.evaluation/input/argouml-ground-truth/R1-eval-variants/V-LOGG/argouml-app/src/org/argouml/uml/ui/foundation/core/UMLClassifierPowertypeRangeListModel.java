package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLClassifierPowertypeRangeListModel extends UMLModelElementListModel2 {
	public UMLClassifierPowertypeRangeListModel() {
		super("powertypeRange");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getPowertypeRanges(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getPowertypeRanges(getTarget()).contains(element);
	}
}



