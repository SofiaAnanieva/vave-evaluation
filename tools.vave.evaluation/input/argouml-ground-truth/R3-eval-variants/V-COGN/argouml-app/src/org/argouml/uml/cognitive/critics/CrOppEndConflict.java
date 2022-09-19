package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrOppEndConflict extends CrUML {
	public CrOppEndConflict() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("associationEnd");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		boolean problem = NO_PROBLEM;
		if (Model.getFacade().isAClassifier(dm)) {
			Collection col = Model.getCoreHelper().getAssociations(dm);
			List names = new ArrayList();
			Iterator it = col.iterator();
			String name = null;
			while (it.hasNext()) {
				name = Model.getFacade().getName(it.next());
				if (name == null||name.equals("")) {
					continue;
				}
				if (names.contains(name)) {
					problem = PROBLEM_FOUND;
					break;
				}
			}
		}
		return problem;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAssociationEnd());
		return ret;
	}
}



