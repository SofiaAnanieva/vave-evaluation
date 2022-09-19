package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrTooManyTransitions extends AbstractCrTooMany {
	private static final int TRANSITIONS_THRESHOLD = 10;
	public CrTooManyTransitions() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STATE_MACHINES);
		setThreshold(TRANSITIONS_THRESHOLD);
		addTrigger("incoming");
		addTrigger("outgoing");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAStateVertex(dm))) {
			return NO_PROBLEM;
		}
		Collection in = Model.getFacade().getIncomings(dm);
		Collection out = Model.getFacade().getOutgoings(dm);
		int inSize = (in == null)?0:in.size();
		int outSize = (out == null)?0:out.size();
		if (inSize + outSize <= getThreshold()) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getStateVertex());
		return ret;
	}
	private static final long serialVersionUID = -5732942378849267065l;
}



