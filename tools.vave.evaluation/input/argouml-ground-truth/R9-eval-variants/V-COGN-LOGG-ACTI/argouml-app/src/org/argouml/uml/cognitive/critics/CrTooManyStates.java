package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrTooManyStates extends AbstractCrTooMany {
	private static final int STATES_THRESHOLD = 20;
	public CrTooManyStates() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		setThreshold(STATES_THRESHOLD);
		addTrigger("substate");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isACompositeState(dm))) {
			return NO_PROBLEM;
		}
		Collection subs = Model.getFacade().getSubvertices(dm);
		if (subs.size() <= getThreshold()) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getCompositeState());
		return ret;
	}
	private static final long serialVersionUID = -7320341818814870066l;
}



