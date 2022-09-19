package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrUnconventionalClassName extends AbstractCrUnconventionalName {
	public CrUnconventionalClassName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))&&!(Model.getFacade().isAInterface(dm))) {
			return NO_PROBLEM;
		}
		Object cls = dm;
		String myName = Model.getFacade().getName(cls);
		if (myName == null||myName.equals("")) {
			return NO_PROBLEM;
		}
		String nameStr = myName;
		if (nameStr == null||nameStr.length() == 0) {
			return NO_PROBLEM;
		}
		char initialChar = nameStr.charAt(0);
		if (Character.isDigit(initialChar)||!Character.isUpperCase(initialChar)) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	@Override public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String sug = Model.getFacade().getName(me);
			sug = computeSuggestion(sug);
			String ins = super.getInstructions();
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
		}
	}
	public String computeSuggestion(String sug) {
		if (sug == null) {
			return"";
		}
		StringBuffer sb = new StringBuffer(sug);
		while (sb.length() > 0&&Character.isDigit(sb.charAt(0))) {
			sb.deleteCharAt(0);
		}
		if (sb.length() == 0) {
			return"";
		}
		return sb.replace(0,1,Character.toString(Character.toUpperCase(sb.charAt(0)))).toString();
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
	private static final long serialVersionUID = -3341858698991522822l;
}



