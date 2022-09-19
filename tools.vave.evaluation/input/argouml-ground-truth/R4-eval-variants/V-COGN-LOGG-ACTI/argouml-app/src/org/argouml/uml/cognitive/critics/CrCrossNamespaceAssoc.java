package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrCrossNamespaceAssoc extends CrUML {
	public CrCrossNamespaceAssoc() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.MODULARITY);
		setKnowledgeTypes(Critic.KT_SYNTAX);
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!Model.getFacade().isAAssociation(dm)) {
			return NO_PROBLEM;
		}
		Object ns = Model.getFacade().getNamespace(dm);
		if (ns == null) {
			return PROBLEM_FOUND;
		}
		Iterator assocEnds = Model.getFacade().getConnections(dm).iterator();
		while (assocEnds.hasNext()) {
			Object clf = Model.getFacade().getType(assocEnds.next());
			if (clf != null&&ns != Model.getFacade().getNamespace(clf)) {
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
}



