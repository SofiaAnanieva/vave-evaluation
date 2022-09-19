package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrUselessInterface extends CrUML {
	public CrUselessInterface() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedGoal(Goal.getUnspecifiedGoal());
		setKnowledgeTypes(Critic.KT_COMPLETENESS);
		addTrigger("realization");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!Model.getFacade().isAInterface(dm)) {
			return NO_PROBLEM;
		}
		if (!Model.getFacade().isPrimaryObject(dm)) {
			return NO_PROBLEM;
		}
		Iterator iter = Model.getFacade().getSupplierDependencies(dm).iterator();
		while (iter.hasNext()) {
			if (Model.getFacade().isRealize(iter.next())) {
				return NO_PROBLEM;
			}
		}
		return PROBLEM_FOUND;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getInterface());
		return ret;
	}
	private static final long serialVersionUID = -6586457111453473553l;
}



