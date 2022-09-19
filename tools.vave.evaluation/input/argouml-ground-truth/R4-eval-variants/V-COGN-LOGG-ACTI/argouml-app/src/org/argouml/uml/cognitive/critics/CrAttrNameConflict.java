package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrAttrNameConflict extends CrUML {
	public CrAttrNameConflict() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedDecision(UMLDecision.STORAGE);
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("structuralFeature");
		addTrigger("feature_name");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm))) {
			return NO_PROBLEM;
		}
		Collection<String>namesSeen = new ArrayList<String>();
		Iterator attrs = Model.getFacade().getAttributes(dm).iterator();
		while (attrs.hasNext()) {
			String name = Model.getFacade().getName(attrs.next());
			if (name == null||name.length() == 0) {
				continue;
			}
			if (namesSeen.contains(name)) {
				return PROBLEM_FOUND;
			}
			namesSeen.add(name);
		}
		return NO_PROBLEM;
	}
	@Override public Icon getClarifier() {
		return ClAttributeCompartment.getTheInstance();
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		return ret;
	}
}



