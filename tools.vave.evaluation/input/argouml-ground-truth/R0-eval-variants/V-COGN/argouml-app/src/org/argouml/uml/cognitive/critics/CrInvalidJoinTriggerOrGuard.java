package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrInvalidJoinTriggerOrGuard extends CrUML {
	public CrInvalidJoinTriggerOrGuard() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		addTrigger("trigger");
		addTrigger("guard");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isATransition(dm))) {
			return NO_PROBLEM;
		}
		Object tr = dm;
		Object t = Model.getFacade().getTrigger(tr);
		Object g = Model.getFacade().getGuard(tr);
		Object dv = Model.getFacade().getTarget(tr);
		if (!(Model.getFacade().isAPseudostate(dv))) {
			return NO_PROBLEM;
		}
		Object k = Model.getFacade().getKind(dv);
		if (!Model.getFacade().equalsPseudostateKind(k,Model.getPseudostateKind().getJoin())) {
			return NO_PROBLEM;
		}
		boolean hasTrigger = (t != null&&Model.getFacade().getName(t) != null&&Model.getFacade().getName(t).length() > 0);
		if (hasTrigger) {
			return PROBLEM_FOUND;
		}
		boolean noGuard = (g == null||Model.getFacade().getExpression(g) == null||Model.getFacade().getBody(Model.getFacade().getExpression(g)) == null||Model.getFacade().getBody(Model.getFacade().getExpression(g)).toString().length() == 0);
		if (!noGuard) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getTransition());
		return ret;
	}
	private static final long serialVersionUID = 1052354516940735748l;
}



