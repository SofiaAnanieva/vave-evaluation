package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementSourceFlowListModel extends UMLModelElementListModel2 {
	public UMLModelElementSourceFlowListModel() {
		super("sourceFlow");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getSourceFlows(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAFlow(o)&&Model.getFacade().getSourceFlows(getTarget()).contains(o);
	}
}



