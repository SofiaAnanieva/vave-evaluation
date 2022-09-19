package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrMultiComposite extends CrUML {
	public CrMultiComposite() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CONTAINMENT);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
		addTrigger("aggregation");
		addTrigger("multiplicity");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		boolean problem = NO_PROBLEM;
		if (Model.getFacade().isAAssociationEnd(dm)) {
			if (Model.getFacade().isComposite(dm)) {
				if (Model.getFacade().getUpper(dm) > 1) {
					problem = PROBLEM_FOUND;
				}
			}
		}
		return problem;
	}
	public Class getWizardClass(ToDoItem item) {
		return WizAssocComposite.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAssociationEnd());
		return ret;
	}
}



