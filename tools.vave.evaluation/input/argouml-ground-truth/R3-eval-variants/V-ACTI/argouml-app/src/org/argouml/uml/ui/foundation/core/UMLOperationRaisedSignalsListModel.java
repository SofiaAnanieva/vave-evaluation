package org.argouml.uml.ui.foundation.core;

import java.util.Collection;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLOperationRaisedSignalsListModel extends UMLModelElementListModel2 {
	public UMLOperationRaisedSignalsListModel() {
		super("signal");
	}
	protected void buildModelList() {
		if (getTarget() != null) {
			Collection signals = null;
			Object target = getTarget();
			if (Model.getFacade().isAOperation(target)) {
				signals = Model.getFacade().getRaisedSignals(target);
			}
			setAllElements(signals);
		}
	}
	protected boolean isValidElement(Object element) {
		Collection signals = null;
		Object target = getTarget();
		if (Model.getFacade().isAOperation(target)) {
			signals = Model.getFacade().getRaisedSignals(target);
		}
		return(signals != null)&&signals.contains(element);
	}
}



