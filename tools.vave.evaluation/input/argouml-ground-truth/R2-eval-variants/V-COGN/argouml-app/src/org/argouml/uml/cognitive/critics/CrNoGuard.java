package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNoGuard extends CrUML {
	public CrNoGuard() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		setKnowledgeTypes(Critic.KT_COMPLETENESS);
		addTrigger("guard");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isATransition(dm))) {
			return NO_PROBLEM;
		}
		Object sourceVertex = Model.getFacade().getSource(dm);
		if (!(Model.getFacade().isAPseudostate(sourceVertex))) {
			return NO_PROBLEM;
		}
		if (!Model.getFacade().equalsPseudostateKind(Model.getFacade().getKind(sourceVertex),Model.getPseudostateKind().getChoice())) {
			return NO_PROBLEM;
		}
		Object guard = Model.getFacade().getGuard(dm);
		boolean noGuard = (guard == null||Model.getFacade().getExpression(guard) == null||Model.getFacade().getBody(Model.getFacade().getExpression(guard)) == null||((String) Model.getFacade().getBody(Model.getFacade().getExpression(guard))).length() == 0);
		if (noGuard) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getTransition());
		return ret;
	}
}



