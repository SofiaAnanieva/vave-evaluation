package org.argouml.uml.ui.foundation.core;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLCommentAnnotatedElementListModel extends UMLModelElementListModel2 {
	public UMLCommentAnnotatedElementListModel() {
		super("annotatedElement");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			setAllElements(Model.getFacade().getAnnotatedElements(getTarget()));
		}
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAModelElement(element)&&Model.getFacade().getAnnotatedElements(getTarget()).contains(element);
	}
}



