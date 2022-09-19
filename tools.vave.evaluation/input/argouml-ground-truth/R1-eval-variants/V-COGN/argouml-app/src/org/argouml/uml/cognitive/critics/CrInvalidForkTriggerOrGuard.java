package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrInvalidForkTriggerOrGuard extends CrUML {
	public CrInvalidForkTriggerOrGuard() {
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
		Object sv = Model.getFacade().getSource(tr);
		if (!(Model.getFacade().isAPseudostate(sv))) {
			return NO_PROBLEM;
		}
		Object k = Model.getFacade().getKind(sv);
		if (!Model.getFacade().equalsPseudostateKind(k,Model.getPseudostateKind().getFork())) {
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
	private static final long serialVersionUID = -713044875133409390l;
}



