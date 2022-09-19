package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoNamespaceToOwnedElements extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.namespace.owned-element");
	}
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isANamespace(parent)) {
			return Collections.EMPTY_LIST;
		}
		Collection ownedElements = Model.getFacade().getOwnedElements(parent);
		Iterator it = ownedElements.iterator();
		Collection ret = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (Model.getFacade().isACollaboration(o)) {
				if ((Model.getFacade().getRepresentedClassifier(o) != null)||(Model.getFacade().getRepresentedOperation(o) != null)) {
					continue;
				}
			}
			if (Model.getFacade().isAStateMachine(o)&&Model.getFacade().getContext(o) != parent) {
				continue;
			}
			if (Model.getFacade().isAComment(o)) {
				if (Model.getFacade().getAnnotatedElements(o).size() != 0) {
					continue;
				}
			}
			ret.add(o);
		}
		return ret;
	}
	public Set getDependencies(Object parent) {
		if (Model.getFacade().isANamespace(parent)) {
			Set set = new HashSet();
			set.add(parent);
			return set;
		}
		return Collections.EMPTY_SET;
	}
}



