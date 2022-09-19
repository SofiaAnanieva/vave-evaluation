package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class TransitionNotation extends NotationProvider {
	public TransitionNotation(Object transition) {
		if (!Model.getFacade().isATransition(transition)) {
			throw new IllegalArgumentException("This is not a Transition.");
		}
	}
}



