package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInstanceLinkEndListModel extends UMLModelElementListModel2 {
	public UMLInstanceLinkEndListModel() {
		super("linkEnd");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getLinkEnds(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getLinkEnds(getTarget()).contains(element);
	}
}



