package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class ObjectFlowStateStateNotation extends NotationProvider {
	public ObjectFlowStateStateNotation(Object objectflowstate) {
		if (!Model.getFacade().isAObjectFlowState(objectflowstate)) {
			throw new IllegalArgumentException("This is not a ObjectFlowState.");
		}
	}
}



