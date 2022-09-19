package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementElementResidenceListModel extends UMLModelElementListModel2 {
	public UMLModelElementElementResidenceListModel() {
		super("elementResidence");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getElementResidences(getTarget()));
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAElementResidence(o)&&Model.getFacade().getElementResidences(getTarget()).contains(o);
	}
}



