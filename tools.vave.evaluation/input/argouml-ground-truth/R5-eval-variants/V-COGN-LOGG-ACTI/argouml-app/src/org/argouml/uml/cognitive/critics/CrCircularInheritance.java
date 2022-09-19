package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrCircularInheritance extends CrUML {
	private static final Logger LOG = Logger.getLogger(CrCircularInheritance.class);
	public CrCircularInheritance() {
		setupHeadAndDesc();
		setPriority(ToDoItem.HIGH_PRIORITY);
		addSupportedDecision(UMLDecision.INHERITANCE);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("generalization");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		boolean problem = NO_PROBLEM;
		if (Model.getFacade().isAGeneralizableElement(dm)) {
			try {
				Model.getCoreHelper().getChildren(dm);
			}catch (IllegalStateException ex) {
				problem = PROBLEM_FOUND;
				LOG.info("problem found for: " + this);
			}
		}
		return problem;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getGeneralizableElement());
		return ret;
	}
}



