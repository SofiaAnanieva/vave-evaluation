package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ListSet;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.util.ChildGenerator;


public class CrUselessAbstract extends CrUML {
	public CrUselessAbstract() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedGoal(Goal.getUnspecifiedGoal());
		addTrigger("specialization");
		addTrigger("isAbstract");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
			return false;
		}
		Object cls = dm;
		if (!Model.getFacade().isAbstract(cls)) {
			return false;
		}
		ListSet derived = (new ListSet(cls)).reachable(new ChildGenDerivedClasses());
		for (Object c:derived) {
			if (!Model.getFacade().isAbstract(c)) {
				return false;
			}
		}
		return true;
	}
	@Override public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
}

class ChildGenDerivedClasses implements ChildGenerator {
	public Iterator childIterator(Object o) {
		Object c = o;
		Collection specs = new ArrayList(Model.getFacade().getSpecializations(c));
		if (specs == null) {
			return Collections.emptySet().iterator();
		}
		List specClasses = new ArrayList(specs.size());
		for (Object g:specs) {
			Object ge = Model.getFacade().getSpecific(g);
			if (ge != null) {
				specClasses.add(ge);
			}
		}
		return specClasses.iterator();
	}
}



