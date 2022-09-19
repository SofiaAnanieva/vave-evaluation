package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrConstructorNeeded extends CrUML {
	public CrConstructorNeeded() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STORAGE);
		addKnowledgeType(Critic.KT_CORRECTNESS);
		addTrigger("behavioralFeature");
		addTrigger("structuralFeature");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isPrimaryObject(dm)))return NO_PROBLEM;
		if (Model.getFacade().isType(dm)) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isUtility(dm)) {
			return NO_PROBLEM;
		}
		Collection operations = Model.getFacade().getOperations(dm);
		Iterator opers = operations.iterator();
		while (opers.hasNext()) {
			if (Model.getFacade().isConstructor(opers.next())) {
				return NO_PROBLEM;
			}
		}
		Iterator attrs = Model.getFacade().getAttributes(dm).iterator();
		while (attrs.hasNext()) {
			Object attr = attrs.next();
			if (Model.getFacade().isStatic(attr))continue;
			if (Model.getFacade().isInitialized(attr))continue;
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizAddConstructor) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String ins = super.getInstructions();
			String sug = null;
			if (me != null)sug = Model.getFacade().getName(me);
			if ("".equals(sug)) {
				sug = super.getDefaultSuggestion();
			}
			((WizAddConstructor) w).setInstructions(ins);
			((WizAddConstructor) w).setSuggestion(sug);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizAddConstructor.class;
	}
}



