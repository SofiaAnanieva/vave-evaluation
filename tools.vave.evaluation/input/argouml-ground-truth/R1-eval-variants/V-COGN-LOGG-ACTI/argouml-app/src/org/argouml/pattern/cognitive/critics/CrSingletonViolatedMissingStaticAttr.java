package org.argouml.pattern.cognitive.critics;

import java.util.Iterator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;


public class CrSingletonViolatedMissingStaticAttr extends CrUML {
	public CrSingletonViolatedMissingStaticAttr() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.PATTERNS);
		setPriority(ToDoItem.MED_PRIORITY);
		addTrigger("stereotype");
		addTrigger("structuralFeature");
		addTrigger("associationEnd");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isSingleton(dm))) {
			return NO_PROBLEM;
		}
		Iterator attrs = Model.getFacade().getAttributes(dm).iterator();
		while (attrs.hasNext()) {
			Object attr = attrs.next();
			if (!(Model.getFacade().isStatic(attr))) {
				continue;
			}
			if (Model.getFacade().getType(attr) == dm) {
				return NO_PROBLEM;
			}
		}
		return PROBLEM_FOUND;
	}
}



