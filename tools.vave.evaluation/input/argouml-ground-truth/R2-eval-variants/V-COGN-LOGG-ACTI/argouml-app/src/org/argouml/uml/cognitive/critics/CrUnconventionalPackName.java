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


public class CrUnconventionalPackName extends AbstractCrUnconventionalName {
	public CrUnconventionalPackName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAPackage(dm))) {
			return NO_PROBLEM;
		}
		String myName = Model.getFacade().getName(dm);
		if (myName == null||myName.equals("")) {
			return NO_PROBLEM;
		}
		String nameStr = myName;
		if (nameStr == null||nameStr.length() == 0) {
			return NO_PROBLEM;
		}
		int size = nameStr.length();
		for (int i = 0;i < size;i++) {
			char c = nameStr.charAt(i);
			if (!Character.isLowerCase(c)) {
				return PROBLEM_FOUND;
			}
		}
		return NO_PROBLEM;
	}
	public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String ins = super.getInstructions();
			String nameStr = Model.getFacade().getName(me);
			String sug = computeSuggestion(nameStr);
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
		}
	}
	public String computeSuggestion(String nameStr) {
		StringBuilder sug = new StringBuilder();
		if (nameStr != null) {
			int size = nameStr.length();
			for (int i = 0;i < size;i++) {
				char c = nameStr.charAt(i);
				if (Character.isLowerCase(c)) {
					sug.append(c);
				}else if (Character.isUpperCase(c)) {
					sug.append(Character.toLowerCase(c));
				}
			}
		}
		if (sug.toString().equals("")) {
			return"packageName";
		}
		return sug.toString();
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getPackage());
		return ret;
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
}



