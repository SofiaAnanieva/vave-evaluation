package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoMessageToAction extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.message.action");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAMessage(parent)) {
			Object action = Model.getFacade().getAction(parent);
			if (action != null) {
				List children = new ArrayList();
				children.add(action);
				return children;
			}
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAMessage(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



