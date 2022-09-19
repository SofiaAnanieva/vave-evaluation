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


public class CrNoInstanceVariables extends CrUML {
	private static final int MAX_DEPTH = 50;
	public CrNoInstanceVariables() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STORAGE);
		setKnowledgeTypes(Critic.KT_COMPLETENESS);
		addTrigger("structuralFeature");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
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
		if (findChangeableInstanceAttributeInInherited(dm,0)) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	@Override public Icon getClarifier() {
		return ClAttributeCompartment.getTheInstance();
	}
	private boolean findChangeableInstanceAttributeInInherited(Object dm,int depth) {
		Iterator attribs = Model.getFacade().getAttributes(dm).iterator();
		while (attribs.hasNext()) {
			Object attr = attribs.next();
			if (!Model.getFacade().isStatic(attr)&&!Model.getFacade().isReadOnly(attr)) {
				return true;
			}
		}
		if (depth > MAX_DEPTH)return false;
		Iterator iter = Model.getFacade().getGeneralizations(dm).iterator();
		while (iter.hasNext()) {
			Object parent = Model.getFacade().getGeneral(iter.next());
			if (parent == dm)continue;
			if (Model.getFacade().isAClassifier(parent)&&findChangeableInstanceAttributeInInherited(parent,depth + 1)) {
				return true;
			}
		}
		return false;
	}
	@Override public void initWizard(Wizard w) {
		if (w instanceof WizAddInstanceVariable) {
			String ins = super.getInstructions();
			String sug = super.getDefaultSuggestion();
			((WizAddInstanceVariable) w).setInstructions(ins);
			((WizAddInstanceVariable) w).setSuggestion(sug);
		}
	}
	@Override public Class getWizardClass(ToDoItem item) {
		return WizAddInstanceVariable.class;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
}



