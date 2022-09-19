package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoAssocRoleToMessages extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.association-role.messages");
	}
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isAAssociationRole(parent)) {
			return Collections.EMPTY_SET;
		}
		return Model.getFacade().getMessages(parent);
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAAssociationRole(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



