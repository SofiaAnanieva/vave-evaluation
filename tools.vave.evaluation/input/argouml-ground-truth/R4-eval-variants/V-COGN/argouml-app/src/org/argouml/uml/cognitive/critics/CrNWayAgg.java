package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNWayAgg extends CrUML {
	public CrNWayAgg() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CONTAINMENT);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
		addTrigger("connection");
		addTrigger("end_aggregation");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAAssociation(dm))) {
			return NO_PROBLEM;
		}
		Object asc = dm;
		if (Model.getFacade().isAAssociationRole(asc)) {
			return NO_PROBLEM;
		}
		Collection conns = Model.getFacade().getConnections(asc);
		if ((conns == null)||(conns.size() <= 2)) {
			return NO_PROBLEM;
		}
		Iterator assocEnds = conns.iterator();
		while (assocEnds.hasNext()) {
			Object ae = assocEnds.next();
			if (Model.getFacade().isAggregate(ae)||Model.getFacade().isComposite(ae)) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAssociationClass());
		return ret;
	}
	private static final long serialVersionUID = 5318978944855930303l;
}



