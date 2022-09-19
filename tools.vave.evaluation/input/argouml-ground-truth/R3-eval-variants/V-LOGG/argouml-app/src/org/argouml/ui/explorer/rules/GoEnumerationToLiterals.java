package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoEnumerationToLiterals extends AbstractPerspectiveRule {
	public GoEnumerationToLiterals() {
		super();
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAEnumeration(parent)) {
			List list = new ArrayList();
			if (Model.getFacade().getEnumerationLiterals(parent) != null&&(Model.getFacade().getEnumerationLiterals(parent).size() > 0)) {
				list.addAll(Model.getFacade().getEnumerationLiterals(parent));
			}
			return list;
		}
		return Collections.EMPTY_SET;
	}
	public String getRuleName() {
		return Translator.localize("misc.enumeration.literal");
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAEnumeration(parent)) {
			Set set = new HashSet();
			set.add(parent);
			set.addAll(Model.getFacade().getEnumerationLiterals(parent));
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



