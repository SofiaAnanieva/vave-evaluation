package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrDisambigClassName extends CrUML {
	public CrDisambigClassName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
		addTrigger("elementOwnership");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm))) {
			return NO_PROBLEM;
		}
		Object classifier = dm;
		String designMaterialName = Model.getFacade().getName(classifier);
		if (designMaterialName != null&&designMaterialName.length() == 0) {
			return NO_PROBLEM;
		}
		Collection elementImports = Model.getFacade().getElementImports2(classifier);
		if (elementImports == null) {
			return NO_PROBLEM;
		}
		for (Iterator iter = elementImports.iterator();iter.hasNext();) {
			Object imp = iter.next();
			Object pack = Model.getFacade().getPackage(imp);
			String alias = Model.getFacade().getAlias(imp);
			if (alias == null||alias.length() == 0) {
				alias = designMaterialName;
			}
			Collection siblings = Model.getFacade().getOwnedElements(pack);
			if (siblings == null) {
				return NO_PROBLEM;
			}
			Iterator elems = siblings.iterator();
			while (elems.hasNext()) {
				Object eo = elems.next();
				Object me = eo;
				if (!(Model.getFacade().isAClassifier(me))) {
					continue;
				}
				if (me == classifier) {
					continue;
				}
				String meName = Model.getFacade().getName(me);
				if (meName == null||meName.equals("")) {
					continue;
				}
				if (meName.equals(alias)) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
	public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	public void initWizard(Wizard w) {
		if (w instanceof WizMEName) {
			ToDoItem item = (ToDoItem) w.getToDoItem();
			Object me = item.getOffenders().get(0);
			String sug = Model.getFacade().getName(me);
			String ins = super.getInstructions();
			((WizMEName) w).setInstructions(ins);
			((WizMEName) w).setSuggestion(sug);
			((WizMEName) w).setMustEdit(true);
		}
	}
	public Class getWizardClass(ToDoItem item) {
		return WizMEName.class;
	}
}



