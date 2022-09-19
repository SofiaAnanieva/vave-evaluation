package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNoOperations extends CrUML {
	public CrNoOperations() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.BEHAVIOR);
		setKnowledgeTypes(Critic.KT_COMPLETENESS);
		addTrigger("behavioralFeature");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm)||Model.getFacade().isAInterface(dm))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isPrimaryObject(dm))) {
			return NO_PROBLEM;
		}
		if ((Model.getFacade().getName(dm) == null)||("".equals(Model.getFacade().getName(dm)))) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isType(dm)) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isUtility(dm)) {
			return NO_PROBLEM;
		}
		if (findInstanceOperationInInherited(dm,0)) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	@Override public Icon getClarifier() {
		return ClOperationCompartment.getTheInstance();
	}
	private boolean findInstanceOperationInInherited(Object dm,int depth) {
		Iterator ops = Model.getFacade().getOperations(dm).iterator();
		while (ops.hasNext()) {
			if (!Model.getFacade().isStatic(ops.next())) {
				return true;
			}
		}
		if (depth > 50)return false;
		Iterator iter = Model.getFacade().getGeneralizations(dm).iterator();
		while (iter.hasNext()) {
			Object parent = Model.getFacade().getGeneral(iter.next());
			if (parent == dm)continue;
			if (Model.getFacade().isAClassifier(parent)&&findInstanceOperationInInherited(parent,depth + 1)) {
				return true;
			}
		}
		return false;
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizAddOperation) {
			String ins = super.getInstructions();
			String sug = super.getDefaultSuggestion();
			((WizAddOperation) w).setInstructions(ins);
			((WizAddOperation) w).setSuggestion(sug);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizAddOperation.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		ret.add(Model.getMetaTypes().getInterface());
		return ret;
	}
}



