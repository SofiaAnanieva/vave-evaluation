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


public class GoNamespaceToClassifierAndPackage extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.namespace.classifer-or-package");
	}
	public Collection getChildren(Object parent) {
		if (!Model.getFacade().isANamespace(parent)) {
			return Collections.EMPTY_SET;
		}
		Iterator elements = Model.getFacade().getOwnedElements(parent).iterator();
		List result = new ArrayList();
		while (elements.hasNext()) {
			Object element = elements.next();
			if (Model.getFacade().isAPackage(element)||Model.getFacade().isAClassifier(element)) {
				result.add(element);
			}
		}
		return result;
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



