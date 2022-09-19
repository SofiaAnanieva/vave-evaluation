package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoSummaryToOutgoingDependency extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.summary.outgoing-dependency");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof OutgoingDependencyNode) {
			Collection list = new ArrayList();
			Iterator it = Model.getFacade().getClientDependencies(((OutgoingDependencyNode) parent).getParent()).iterator();
			while (it.hasNext()) {
				Object next = it.next();
				if (!Model.getFacade().isAAbstraction(next)) {
					list.add(next);
				}
			}
			return list;
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		if (parent instanceof OutgoingDependencyNode) {
			Set set = new HashSet();
			set.add(((OutgoingDependencyNode) parent).getParent());
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



