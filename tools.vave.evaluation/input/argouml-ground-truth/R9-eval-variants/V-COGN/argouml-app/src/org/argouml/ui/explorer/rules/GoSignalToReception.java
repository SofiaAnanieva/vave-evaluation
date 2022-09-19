package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoSignalToReception extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("Signal->Reception");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isASignal(parent)) {
			return Model.getFacade().getReceptions(parent);
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isASignal(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



