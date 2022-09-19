package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;


public class CrUnconventionalOperName extends AbstractCrUnconventionalName {
	public CrUnconventionalOperName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("feature_name");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAOperation(dm))) {
			return NO_PROBLEM;
		}
		Object oper = dm;
		String myName = Model.getFacade().getName(oper);
		if (myName == null||myName.equals("")) {
			return NO_PROBLEM;
		}
		String nameStr = myName;
		if (nameStr == null||nameStr.length() == 0) {
			return NO_PROBLEM;
		}
		char initalChar = nameStr.charAt(0);
		for (Object stereo:Model.getFacade().getStereotypes(oper)) {
			if ("create".equals(Model.getFacade().getName(stereo))||"constructor".equals(Model.getFacade().getName(stereo))) {
				return NO_PROBLEM;
			}
		}
		if (!Character.isLowerCase(initalChar)) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		Object f = dm;
		ListSet offs = computeOffenders(f);
		return new UMLToDoItem(this,offs,dsgr);
	}
	protected ListSet computeOffenders(Object dm) {
		ListSet offs = new ListSet(dm);
		offs.add(Model.getFacade().getOwner(dm));
		return offs;
	}
	@Override public boolean stillValid(ToDoItem i,Designer dsgr) {
		if (!isActive()) {
			return false;
		}
		ListSet offs = i.getOffenders();
		Object f = offs.get(0);
		if (!predicate(f,dsgr)) {
			return false;
		}
		ListSet newOffs = computeOffenders(f);
		boolean res = offs.equals(newOffs);
		return res;
	}
	protected boolean candidateForConstructor(Object me) {
		if (!(Model.getFacade().isAOperation(me))) {
			return false;
		}
		Object oper = me;
		String myName = Model.getFacade().getName(oper);
		if (myName == null||myName.equals("")) {
			return false;
		}
		Object cl = Model.getFacade().getOwner(oper);
		String nameCl = Model.getFacade().getName(cl);
		if (nameCl == null||nameCl.equals("")) {
			return false;
		}
		if (myName.equals(nameCl)) {
			return true;
		}
		return false;
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizOperName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String sug = Model.getFacade().getName(me);
			sug = computeSuggestion(sug);
			boolean cand = candidateForConstructor(me);
			String ins;
			if (cand) {
				ins = super.getLocalizedString("-ins-ext");
			}else {
				ins = super.getInstructions();
			}
			((WizOperName) w).setInstructions(ins);
			((WizOperName) w).setSuggestion(sug);
			((WizOperName) w).setPossibleConstructor(cand);
		}
	}
	public String computeSuggestion(String sug) {
		if (sug == null) {
			return"";
		}
		return sug.substring(0,1).toLowerCase() + sug.substring(1);
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizOperName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getOperation());
		return ret;
	}
}



