package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoStimulusToAction extends AbstractPerspectiveRule {
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isAStimulus(parent)) {
			return Collections.EMPTY_SET;
		}
		Object ms = parent;
		Object action = Model.getFacade().getDispatchAction(ms);
		Collection result = new ArrayList();
		result.add(action);
		return result;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAStimulus(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
	public String getRuleName() {
		return Translator.localize("misc.stimulus.action");
	}
}



