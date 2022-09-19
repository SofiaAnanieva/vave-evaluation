package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrConflictingComposites extends CrUML {
	public CrConflictingComposites() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CONTAINMENT);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
	}
	public boolean predicate2(Object classifier,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(classifier))) {
			return NO_PROBLEM;
		}
		Collection conns = Model.getFacade().getAssociationEnds(classifier);
		if (conns == null) {
			return NO_PROBLEM;
		}
		int compositeCount = 0;
		Iterator assocEnds = conns.iterator();
		while (assocEnds.hasNext()) {
			Object myEnd = assocEnds.next();
			if (Model.getCoreHelper().equalsAggregationKind(myEnd,"composite")) {
				continue;
			}
			if (Model.getFacade().getLower(myEnd) == 0) {
				continue;
			}
			Object asc = Model.getFacade().getAssociation(myEnd);
			if (asc != null&&Model.getCoreHelper().hasCompositeEnd(asc)) {
				compositeCount++;
			}
		}
		if (compositeCount > 1) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
}



