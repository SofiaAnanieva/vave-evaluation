package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrIllegalGeneralization extends CrUML {
	public CrIllegalGeneralization() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addTrigger("supertype");
		addTrigger("subtype");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAGeneralization(dm))) {
			return NO_PROBLEM;
		}
		Object gen = dm;
		Object cls1 = Model.getFacade().getGeneral(gen);
		Object cls2 = Model.getFacade().getSpecific(gen);
		if (cls1 == null||cls2 == null) {
			return NO_PROBLEM;
		}
		java.lang.
				Class javaClass1 = cls1.getClass();
		java.lang.
				Class javaClass2 = cls2.getClass();
		if (javaClass1 != javaClass2) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getGeneralizableElement());
		return ret;
	}
}



