package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrAlreadyRealizes extends CrUML {
	public CrAlreadyRealizes() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		setKnowledgeTypes(Critic.KT_SEMANTICS,Critic.KT_PRESENTATION);
		addTrigger("generalization");
		addTrigger("realization");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		boolean problem = NO_PROBLEM;
		if (Model.getFacade().isAClass(dm)) {
			Collection col = Model.getCoreHelper().getAllRealizedInterfaces(dm);
			Set set = new HashSet();
			set.addAll(col);
			if (set.size() < col.size()) {
				problem = PROBLEM_FOUND;
			}
		}
		return problem;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
	private static final long serialVersionUID = -8264991005828634274l;
}



