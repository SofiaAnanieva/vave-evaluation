package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class ExtensionPointNotation extends NotationProvider {
	public ExtensionPointNotation(Object ep) {
		if (!Model.getFacade().isAExtensionPoint(ep)) {
			throw new IllegalArgumentException("This is not an ExtensionPoint.");
		}
	}
}



