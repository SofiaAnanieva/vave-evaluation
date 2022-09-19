package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLGeneralizableElementSpecializationListModel extends UMLModelElementListModel2 {
	public UMLGeneralizableElementSpecializationListModel() {
		super("specialization",Model.getMetaTypes().getGeneralization(),true);
	}
	@Override protected void buildModelList() {
		if (getTarget() != null&&Model.getFacade().isAGeneralizableElement(getTarget())) {
			setAllElements(Model.getFacade().getSpecializations(getTarget()));
		}
	}
	@Override protected boolean isValidElement(Object element) {
		return Model.getFacade().isAGeneralization(element)&&Model.getFacade().getSpecializations(getTarget()).contains(element);
	}
}



