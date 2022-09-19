package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoSummaryToAssociation extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.summary.association");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof AssociationsNode) {
			return Model.getCoreHelper().getAssociations(((AssociationsNode) parent).getParent());
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (parent instanceof AssociationsNode) {
			Set set = new HashSet();
			set.add(((AssociationsNode) parent).getParent());
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



