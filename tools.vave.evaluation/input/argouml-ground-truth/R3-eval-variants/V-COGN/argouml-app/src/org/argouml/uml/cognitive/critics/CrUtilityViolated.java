package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrUtilityViolated extends CrUML {
	public CrUtilityViolated() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STORAGE);
		addSupportedDecision(UMLDecision.STEREOTYPES);
		addSupportedDecision(UMLDecision.CLASS_SELECTION);
		addTrigger("stereotype");
		addTrigger("behavioralFeature");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isUtility(dm))) {
			return NO_PROBLEM;
		}
		Collection classesToCheck = new ArrayList();
		classesToCheck.addAll(Model.getCoreHelper().getSupertypes(dm));
		classesToCheck.addAll(Model.getCoreHelper().getAllRealizedInterfaces(dm));
		classesToCheck.add(dm);
		Iterator it = classesToCheck.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (!Model.getFacade().isAInterface(o)) {
				Iterator it2 = Model.getFacade().getAttributes(o).iterator();
				while (it2.hasNext()) {
					if (!Model.getFacade().isStatic(it2.next())) {
						return PROBLEM_FOUND;
					}
				}
			}
			Iterator it2 = Model.getFacade().getOperations(o).iterator();
			while (it2.hasNext()) {
				if (!Model.getFacade().isStatic(it2.next())) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		return ret;
	}
}



