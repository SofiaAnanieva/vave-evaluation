package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrNonAggDataType extends CrUML {
	public CrNonAggDataType() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CONTAINMENT);
		addSupportedDecision(UMLDecision.CLASS_SELECTION);
		setKnowledgeTypes(Critic.KT_SYNTAX);
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getDataType());
		return ret;
	}
}



