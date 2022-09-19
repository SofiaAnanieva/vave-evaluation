package org.argouml.profile.internal.ocl;

import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.profile.internal.ocl.uml14.Uml14ModelInterpreter;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;


public class CrOCL extends CrUML {
	private OclInterpreter interpreter = null;
	private String ocl = null;
	private Set<Object>designMaterials;
	public CrOCL(String oclConstraint,String headline,String description,Integer priority,List<Decision>supportedDecisions,List<String>knowledgeTypes,String moreInfoURL)throws InvalidOclException {
		interpreter = new OclInterpreter(oclConstraint,new Uml14ModelInterpreter());
		this.ocl = oclConstraint;
		addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
		setPriority(ToDoItem.HIGH_PRIORITY);
		List<String>triggers = interpreter.getTriggers();
		designMaterials = interpreter.getCriticizedDesignMaterials();
		for (String string:triggers) {
			addTrigger(string);
		}
		if (headline == null) {
			super.setHeadline("OCL Expression");
		}else {
			super.setHeadline(headline);
		}
		if (description == null) {
			super.setDescription("");
		}else {
			super.setDescription(description);
		}
		if (priority == null) {
			setPriority(ToDoItem.HIGH_PRIORITY);
		}else {
			setPriority(priority);
		}
		if (supportedDecisions != null) {
			for (Decision d:supportedDecisions) {
				addSupportedDecision(d);
			}
		}
		if (knowledgeTypes != null) {
			for (String k:knowledgeTypes) {
				addKnowledgeType(k);
			}
		}
		if (moreInfoURL != null) {
			setMoreInfoURL(moreInfoURL);
		}
	}
	@Override public Set<Object>getCriticizedDesignMaterials() {
		return designMaterials;
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!interpreter.applicable(dm)) {
			return NO_PROBLEM;
		}else {
			if (interpreter.check(dm)) {
				return NO_PROBLEM;
			}else {
				return PROBLEM_FOUND;
			}
		}
	}
	public String getOCL() {
		return ocl;
	}
}



