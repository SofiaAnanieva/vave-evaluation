package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoClassifierToStructuralFeature extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.class.attribute");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAClassifier(parent)) {
			return Model.getFacade().getStructuralFeatures(parent);
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAClassifier(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



