package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrFinalSubclassed extends CrUML {
	public CrFinalSubclassed() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
		addTrigger("specialization");
		addTrigger("isLeaf");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!Model.getFacade().isAGeneralizableElement(dm)) {
			return NO_PROBLEM;
		}
		if (!Model.getFacade().isLeaf(dm)) {
			return NO_PROBLEM;
		}
		Iterator specs = Model.getFacade().getSpecializations(dm).iterator();
		return specs.hasNext()?PROBLEM_FOUND:NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		ret.add(Model.getMetaTypes().getInterface());
		return ret;
	}
}



