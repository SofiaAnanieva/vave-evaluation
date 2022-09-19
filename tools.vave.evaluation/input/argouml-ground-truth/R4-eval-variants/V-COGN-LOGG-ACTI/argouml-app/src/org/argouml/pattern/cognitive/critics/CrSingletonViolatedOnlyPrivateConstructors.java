package org.argouml.pattern.cognitive.critics;

import java.util.Iterator;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;


public class CrSingletonViolatedOnlyPrivateConstructors extends CrUML {
	public CrSingletonViolatedOnlyPrivateConstructors() {
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
		Iterator operations = Model.getFacade().getOperations(dm).iterator();
		while (operations.hasNext()) {
			Object o = operations.next();
			if (!(Model.getFacade().isConstructor(o))) {
				continue;
			}
			if (!(Model.getFacade().isPrivate(o))) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
}



