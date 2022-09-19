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


public class CrMissingAttrName extends CrUML {
	public CrMissingAttrName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAAttribute(dm)))return NO_PROBLEM;
		Object attr = dm;
		String myName = Model.getFacade().getName(attr);
		if (myName == null||"".equals(myName))return PROBLEM_FOUND;
		if (myName.length() == 0)return PROBLEM_FOUND;
		return NO_PROBLEM;
	}
	public Icon getClarifier() {
		return ClAttributeCompartment.getTheInstance();
	}
	public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String ins = super.getInstructions();
			String sug = super.getDefaultSuggestion();
			if (Model.getFacade().isAAttribute(me)) {
				Object a = me;
				int count = 1;
				if (Model.getFacade().getOwner(a) != null)count = Model.getFacade().getFeatures(Model.getFacade().getOwner(a)).size();
				sug = "attr" + (count + 1);
			}
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
		}
	}
	public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAttribute());
		return ret;
	}
}



