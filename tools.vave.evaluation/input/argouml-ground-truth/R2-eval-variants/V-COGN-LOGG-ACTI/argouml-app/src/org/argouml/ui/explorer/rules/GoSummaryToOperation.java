package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoSummaryToOperation extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.summary.operation");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof OperationsNode) {
			return Model.getFacade().getOperations(((OperationsNode) parent).getParent());
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (parent instanceof OperationsNode) {
			Set set = new HashSet();
			set.add(((OperationsNode) parent).getParent());
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



