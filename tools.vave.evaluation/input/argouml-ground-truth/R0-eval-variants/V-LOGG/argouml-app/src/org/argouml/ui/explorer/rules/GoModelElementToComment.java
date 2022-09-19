package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoModelElementToComment extends AbstractPerspectiveRule {
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAModelElement(parent)) {
			return Model.getFacade().getComments(parent);
		}
		return Collections.EMPTY_SET;
	}
	public String getRuleName() {
		return Translator.localize("misc.model-element.comment");
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAModelElement(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



