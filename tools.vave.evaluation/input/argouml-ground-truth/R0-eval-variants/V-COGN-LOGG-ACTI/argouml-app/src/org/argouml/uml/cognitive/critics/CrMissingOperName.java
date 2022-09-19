package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrMissingOperName extends CrUML {
	public CrMissingOperName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAOperation(dm)))return NO_PROBLEM;
		Object oper = dm;
		String myName = Model.getFacade().getName(oper);
		if (myName == null||myName.equals(""))return PROBLEM_FOUND;
		if (myName.length() == 0)return PROBLEM_FOUND;
		return NO_PROBLEM;
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String ins = super.getInstructions();
			String sug = super.getDefaultSuggestion();
			if (Model.getFacade().isAOperation(me)) {
				Object a = me;
				int count = 1;
				if (Model.getFacade().getOwner(a) != null)count = Model.getFacade().getFeatures(Model.getFacade().getOwner(a)).size();
				sug = "oper" + (count + 1);
			}
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getOperation());
		return ret;
	}
}



