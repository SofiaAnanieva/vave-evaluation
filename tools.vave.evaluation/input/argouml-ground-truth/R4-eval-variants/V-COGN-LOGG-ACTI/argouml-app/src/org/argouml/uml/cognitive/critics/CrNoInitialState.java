package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNoInitialState extends CrUML {
	public CrNoInitialState() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		addTrigger("substate");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isACompositeState(dm))) {
			return NO_PROBLEM;
		}
		Object cs = dm;
		if (Model.getFacade().getStateMachine(cs) == null) {
			return NO_PROBLEM;
		}
		Collection peers = Model.getFacade().getSubvertices(cs);
		int initialStateCount = 0;
		if (peers == null) {
			return PROBLEM_FOUND;
		}
		for (Iterator iter = peers.iterator();iter.hasNext();) {
			Object sv = iter.next();
			if (Model.getFacade().isAPseudostate(sv)&&(Model.getFacade().getKind(sv).equals(Model.getPseudostateKind().getInitial()))) {
				initialStateCount++;
			}
		}
		if (initialStateCount == 0) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getCompositeState());
		return ret;
	}
}



