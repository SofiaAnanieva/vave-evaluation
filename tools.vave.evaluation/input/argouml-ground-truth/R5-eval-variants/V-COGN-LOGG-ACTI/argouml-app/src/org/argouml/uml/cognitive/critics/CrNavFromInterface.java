package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNavFromInterface extends CrUML {
	public CrNavFromInterface() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("end_navigable");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAAssociation(dm))) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isAAssociationRole(dm)) {
			return NO_PROBLEM;
		}
		Iterator assocEnds = Model.getFacade().getConnections(dm).iterator();
		boolean haveInterfaceEnd = false;
		boolean otherEndNavigable = false;
		while (assocEnds.hasNext()) {
			Object ae = assocEnds.next();
			Object type = Model.getFacade().getType(ae);
			if (Model.getFacade().isAInterface(type)) {
				haveInterfaceEnd = true;
			}else if (Model.getFacade().isNavigable(ae)) {
				otherEndNavigable = true;
			}
			if (haveInterfaceEnd&&otherEndNavigable) {
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
	private static final long serialVersionUID = 2660051106458704056l;
}



