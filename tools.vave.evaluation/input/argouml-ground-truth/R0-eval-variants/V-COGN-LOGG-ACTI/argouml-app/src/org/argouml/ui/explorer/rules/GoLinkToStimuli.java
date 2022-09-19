package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoLinkToStimuli extends AbstractPerspectiveRule {
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isALink(parent)) {
			return Collections.EMPTY_SET;
		}
		return Model.getFacade().getStimuli(parent);
	}
	public String getRuleName() {
		return Translator.localize("misc.link.stimuli");
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isALink(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



