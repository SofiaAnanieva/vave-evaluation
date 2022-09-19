package org.argouml.uml.ui.foundation.core;

import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLOperationMethodsListModel extends UMLModelElementListModel2 {
	public UMLOperationMethodsListModel() {
		super("method");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			Collection methods = null;
			Object target = getTarget();
			if (Model.getFacade().isAOperation(target)) {
				methods = Model.getFacade().getMethods(target);
			}
			setAllElements(methods);
		}
	}
	protected boolean isValidElement(Object element) {
		Collection methods = null;
		Object target = getTarget();
		if (Model.getFacade().isAOperation(target)) {
			methods = Model.getFacade().getMethods(target);
		}
		return(methods != null)&&methods.contains(element);
	}
	private static final long serialVersionUID = -6905298765859760688l;
}



