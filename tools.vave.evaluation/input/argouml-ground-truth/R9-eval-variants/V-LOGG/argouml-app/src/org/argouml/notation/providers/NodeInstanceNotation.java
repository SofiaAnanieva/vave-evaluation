package org.argouml.notation.providers;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class NodeInstanceNotation extends NotationProvider {
	public NodeInstanceNotation(Object nodeInstance) {
		if (!Model.getFacade().isANodeInstance(nodeInstance)) {
			throw new IllegalArgumentException("This is not a NodeInstance.");
		}
	}
}



