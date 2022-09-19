package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoStateToDownstream extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.state.outgoing-states");
	}
	public Collection getChildren(Object parent) {
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAStateVertex(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



