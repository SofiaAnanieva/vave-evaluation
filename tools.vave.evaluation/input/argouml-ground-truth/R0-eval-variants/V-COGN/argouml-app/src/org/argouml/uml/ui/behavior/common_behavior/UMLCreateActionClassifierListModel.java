package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLCreateActionClassifierListModel extends UMLModelElementListModel2 {
	public UMLCreateActionClassifierListModel() {
		super("instantiation");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getCommonBehaviorHelper().getInstantiation(getTarget()));
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().isAClassifier(elem)&&Model.getCommonBehaviorHelper().getInstantiation(getTarget()) == elem;
	}
	private static final long serialVersionUID = -3653652920890159417l;
}



