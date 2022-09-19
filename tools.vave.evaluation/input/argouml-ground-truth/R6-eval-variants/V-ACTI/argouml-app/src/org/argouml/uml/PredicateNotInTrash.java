package org.argouml.uml;

import org.argouml.kernel.ProjectManager;


public class PredicateNotInTrash implements org.argouml.util.Predicate,org.tigris.gef.util.Predicate {
	@Deprecated public boolean predicate(Object obj) {
		return evaluate(obj);
	}
	public boolean evaluate(Object obj) {
		return!ProjectManager.getManager().getCurrentProject().isInTrash(obj);
	}
}



