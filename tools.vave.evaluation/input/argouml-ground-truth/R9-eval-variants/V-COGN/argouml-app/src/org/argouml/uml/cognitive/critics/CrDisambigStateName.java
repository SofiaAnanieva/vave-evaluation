package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrDisambigStateName extends CrUML {
	public CrDisambigStateName() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("name");
		addTrigger("parent");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAState(dm))) {
			return NO_PROBLEM;
		}
		String myName = Model.getFacade().getName(dm);
		if (myName == null||myName.equals("")) {
			return NO_PROBLEM;
		}
		String myNameString = myName;
		if (myNameString.length() == 0) {
			return NO_PROBLEM;
		}
		Collection pkgs = Model.getFacade().getElementImports2(dm);
		if (pkgs == null) {
			return NO_PROBLEM;
		}
		for (Iterator iter = pkgs.iterator();iter.hasNext();) {
			Object imp = iter.next();
			Object ns = Model.getFacade().getPackage(imp);
			if (ns == null) {
				return NO_PROBLEM;
			}
			Collection oes = Model.getFacade().getOwnedElements(ns);
			if (oes == null) {
				return NO_PROBLEM;
			}
			Iterator elems = oes.iterator();
			while (elems.hasNext()) {
				Object eo = elems.next();
				Object me = Model.getFacade().getModelElement(eo);
				if (!(Model.getFacade().isAClassifier(me))) {
					continue;
				}
				if (me == dm) {
					continue;
				}
				String meName = Model.getFacade().getName(me);
				if (meName == null||meName.equals("")) {
					continue;
				}
				if (meName.equals(myNameString)) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
	public Icon getClarifier() {
		return ClClassName.getTheInstance();
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getState());
		return ret;
	}
	private static final long serialVersionUID = 5027208502429769593l;
}



