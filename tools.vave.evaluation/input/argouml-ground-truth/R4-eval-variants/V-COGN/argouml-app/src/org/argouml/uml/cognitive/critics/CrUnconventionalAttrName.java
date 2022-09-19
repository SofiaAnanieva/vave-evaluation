package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;


public class CrUnconventionalAttrName extends AbstractCrUnconventionalName {
	public CrUnconventionalAttrName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("feature_name");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!Model.getFacade().isAAttribute(dm)) {
			return NO_PROBLEM;
		}
		Object attr = dm;
		String nameStr = Model.getFacade().getName(attr);
		if (nameStr == null||nameStr.equals("")) {
			return NO_PROBLEM;
		}
		int pos = 0;
		int length = nameStr.length();
		for (;pos < length&&nameStr.charAt(pos) == '_';pos++) {
		}
		if (pos >= length) {
			return PROBLEM_FOUND;
		}
		char initalChar = nameStr.charAt(pos);
		boolean allCapitals = true;
		for (;pos < length;pos++) {
			if (!Character.isUpperCase(nameStr.charAt(pos))&&nameStr.charAt(pos) != '_') {
				allCapitals = false;
				break;
			}
		}
		if (allCapitals) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isReadOnly(attr)) {
			return NO_PROBLEM;
		}
		if (!Character.isLowerCase(initalChar)) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public ToDoItem toDoItem(Object dm,Designer dsgr) {
		Object f = dm;
		ListSet offs = computeOffenders(f);
		return new UMLToDoItem(this,offs,dsgr);
	}
	protected ListSet computeOffenders(Object dm) {
		ListSet offs = new ListSet(dm);
		offs.add(Model.getFacade().getOwner(dm));
		return offs;
	}
	public String computeSuggestion(String name) {
		String sug;
		int nu;
		if (name == null) {
			return"attr";
		}
		for (nu = 0;nu < name.length();nu++) {
			if (name.charAt(nu) != '_') {
				break;
			}
		}
		if (nu > 0) {
			sug = name.substring(0,nu);
		}else {
			sug = "";
		}
		if (nu < name.length()) {
			sug += Character.toLowerCase(name.charAt(nu));
		}
		if (nu + 1 < name.length()) {
			sug += name.substring(nu + 1);
		}
		return sug;
	}
	public Icon getClarifier() {
		return ClAttributeCompartment.getTheInstance();
	}
	public boolean stillValid(ToDoItem i,Designer dsgr) {
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
	public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String sug = computeSuggestion(Model.getFacade().getName(me));
			String ins = super.getInstructions();
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAttribute());
		return ret;
	}
	private static final long serialVersionUID = 4741909365018862474l;
}



