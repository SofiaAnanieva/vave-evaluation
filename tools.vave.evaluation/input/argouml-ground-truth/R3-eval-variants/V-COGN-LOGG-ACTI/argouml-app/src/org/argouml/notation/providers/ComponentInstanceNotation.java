package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class ComponentInstanceNotation extends NotationProvider {
	public ComponentInstanceNotation(Object componentInstance) {
		if (!Model.getFacade().isAComponentInstance(componentInstance)) {
			throw new IllegalArgumentException("This is not a ComponentInstance.");
		}
	}
}



