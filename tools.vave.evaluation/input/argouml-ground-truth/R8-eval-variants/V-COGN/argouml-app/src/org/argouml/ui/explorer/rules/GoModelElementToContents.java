package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoModelElementToContents extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.model-element.contents");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAModelElement(parent)) {
			return Model.getFacade().getModelElementContents(parent);
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		Set set = new HashSet();
		if (Model.getFacade().isAModelElement(parent)) {
			set.add(parent);
			set.addAll(Model.getFacade().getModelElementContents(parent));
		}
		return set;
	}
}



