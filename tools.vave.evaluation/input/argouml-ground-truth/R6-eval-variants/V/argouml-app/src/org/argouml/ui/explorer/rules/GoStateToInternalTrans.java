package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoStateToInternalTrans extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.state.internal-transitions");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAState(parent)) {
			return Model.getFacade().getInternalTransitions(parent);
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAState(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



