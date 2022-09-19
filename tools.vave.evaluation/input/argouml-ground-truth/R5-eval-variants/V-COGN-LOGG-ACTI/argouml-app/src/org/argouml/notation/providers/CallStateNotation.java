package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class CallStateNotation extends NotationProvider {
	public CallStateNotation(Object callState) {
		if (!Model.getFacade().isACallState(callState)) {
			throw new IllegalArgumentException("This is not an CallState.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"entry","name","remove"});
		Object entryAction = Model.getFacade().getEntry(modelElement);
		if (Model.getFacade().isACallAction(entryAction)) {
			addElementListener(listener,entryAction,"operation");
			Object operation = Model.getFacade().getOperation(entryAction);
			if (operation != null) {
				addElementListener(listener,operation,new String[] {"owner","name"});
				Object classifier = Model.getFacade().getOwner(operation);
				addElementListener(listener,classifier,"name");
			}
		}
	}
}



