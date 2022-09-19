package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoClassToNavigableClass extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.class.navigable-class");
	}
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isAClass(parent)) {
			return Collections.EMPTY_SET;
		}
		List childClasses = new ArrayList();
		Collection ends = Model.getFacade().getAssociationEnds(parent);
		if (ends == null) {
			return Collections.EMPTY_SET;
		}
		Iterator it = ends.iterator();
		while (it.hasNext()) {
			Object ae = it.next();
			Object asc = Model.getFacade().getAssociation(ae);
			Collection allEnds = Model.getFacade().getConnections(asc);
			Object otherEnd = null;
			Iterator endIt = allEnds.iterator();
			if (endIt.hasNext()) {
				otherEnd = endIt.next();
				if (ae != otherEnd&&endIt.hasNext()) {
					otherEnd = endIt.next();
					if (ae != otherEnd) {
						otherEnd = null;
					}
				}
			}
			if (otherEnd == null) {
				continue;
			}
			if (!Model.getFacade().isNavigable(otherEnd)) {
				continue;
			}
			if (childClasses.contains(Model.getFacade().getType(otherEnd))) {
				continue;
			}
			childClasses.add(Model.getFacade().getType(otherEnd));
		}
		return childClasses;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isAClass(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



