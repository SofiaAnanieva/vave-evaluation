package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNoIncomingTransitions extends CrUML {
	public CrNoIncomingTransitions() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		addTrigger("incoming");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAStateVertex(dm))) {
			return NO_PROBLEM;
		}
		Object sv = dm;
		if (Model.getFacade().isAState(sv)) {
			Object sm = Model.getFacade().getStateMachine(sv);
			if (sm != null&&Model.getFacade().getTop(sm) == sv) {
				return NO_PROBLEM;
			}
		}
		if (Model.getFacade().isAPseudostate(sv)) {
			Object k = Model.getFacade().getKind(sv);
			if (k.equals(Model.getPseudostateKind().getChoice())) {
				return NO_PROBLEM;
			}
			if (k.equals(Model.getPseudostateKind().getJunction())) {
				return NO_PROBLEM;
			}
		}
		Collection incoming = Model.getFacade().getIncomings(sv);
		boolean needsIncoming = incoming == null||incoming.size() == 0;
		if (Model.getFacade().isAPseudostate(sv)) {
			if (Model.getFacade().getKind(sv).equals(Model.getPseudostateKind().getInitial())) {
				needsIncoming = false;
			}
		}
		if (needsIncoming) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getStateVertex());
		return ret;
	}
}



