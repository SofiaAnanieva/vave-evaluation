package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoTransitionToSource extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.transition.source-state");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isATransition(parent)) {
			Collection col = new ArrayList();
			col.add(Model.getFacade().getSource(parent));
			return col;
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isATransition(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



