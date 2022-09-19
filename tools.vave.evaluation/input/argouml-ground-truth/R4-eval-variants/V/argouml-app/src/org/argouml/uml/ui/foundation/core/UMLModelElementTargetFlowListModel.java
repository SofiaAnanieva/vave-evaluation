package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLModelElementTargetFlowListModel extends UMLModelElementListModel2 {
	public UMLModelElementTargetFlowListModel() {
		super("targetFlow");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getTargetFlows(getTarget()));
		}
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAFlow(o)&&Model.getFacade().getTargetFlows(getTarget()).contains(o);
	}
}



