package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrCircularAssocClass extends CrUML {
	public CrCircularAssocClass() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!Model.getFacade().isAAssociationClass(dm)) {
			return NO_PROBLEM;
		}
		Collection participants = Model.getFacade().getConnections(dm);
		if (participants == null) {
			return NO_PROBLEM;
		}
		Iterator iter = participants.iterator();
		while (iter.hasNext()) {
			Object aEnd = iter.next();
			if (Model.getFacade().isAAssociationEnd(aEnd)) {
				Object type = Model.getFacade().getType(aEnd);
				if (Model.getFacade().isAAssociationClass(type)) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAssociationClass());
		return ret;
	}
	private static final long serialVersionUID = 5265695413303517728l;
}



