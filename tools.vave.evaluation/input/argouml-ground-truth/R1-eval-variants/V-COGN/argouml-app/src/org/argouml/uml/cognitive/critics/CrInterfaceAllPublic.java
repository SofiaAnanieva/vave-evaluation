package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrInterfaceAllPublic extends CrUML {
	public CrInterfaceAllPublic() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("behavioralFeature");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAInterface(dm))) {
			return NO_PROBLEM;
		}
		Object inf = dm;
		Collection bf = Model.getFacade().getFeatures(inf);
		if (bf == null) {
			return NO_PROBLEM;
		}
		Iterator features = bf.iterator();
		while (features.hasNext()) {
			Object f = features.next();
			if (Model.getFacade().getVisibility(f) == null) {
				return NO_PROBLEM;
			}
			if (!Model.getFacade().getVisibility(f).equals(Model.getVisibilityKind().getPublic())) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getInterface());
		return ret;
	}
}



