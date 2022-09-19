package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class MultiplicityNotation extends NotationProvider {
	public MultiplicityNotation(Object multiplicityOwner) {
		Model.getFacade().getMultiplicity(multiplicityOwner);
	}
}



