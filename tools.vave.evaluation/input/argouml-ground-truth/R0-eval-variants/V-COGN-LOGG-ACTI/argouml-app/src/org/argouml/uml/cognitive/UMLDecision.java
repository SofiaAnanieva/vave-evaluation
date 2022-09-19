package org.argouml.uml.cognitive;

import org.argouml.cognitive.Decision;


public class UMLDecision extends Decision {
	public static final UMLDecision INHERITANCE = new UMLDecision("misc.decision.inheritance",1);
	public static final UMLDecision CONTAINMENT = new UMLDecision("misc.decision.containment",1);
	public static final UMLDecision PATTERNS = new UMLDecision("misc.decision.design-patterns",1);
	public static final UMLDecision RELATIONSHIPS = new UMLDecision("misc.decision.relationships",1);
	public static final UMLDecision STORAGE = new UMLDecision("misc.decision.storage",1);
	public static final UMLDecision BEHAVIOR = new UMLDecision("misc.decision.behavior",1);
	public static final UMLDecision INSTANCIATION = new UMLDecision("misc.decision.instantiation",1);
	public static final UMLDecision NAMING = new UMLDecision("misc.decision.naming",1);
	public static final UMLDecision MODULARITY = new UMLDecision("misc.decision.modularity",1);
	public static final UMLDecision CLASS_SELECTION = new UMLDecision("misc.decision.class-selection",1);
	public static final UMLDecision EXPECTED_USAGE = new UMLDecision("misc.decision.expected-usage",1);
	public static final UMLDecision METHODS = new UMLDecision("misc.decision.methods",1);
	public static final UMLDecision CODE_GEN = new UMLDecision("misc.decision.code-generation",1);
	public static final UMLDecision PLANNED_EXTENSIONS = new UMLDecision("misc.decision.planned-extensions",1);
	public static final UMLDecision STEREOTYPES = new UMLDecision("misc.decision.stereotypes",1);
	public static final UMLDecision STATE_MACHINES = new UMLDecision("misc.decision.mstate-machines",1);
	public UMLDecision(String name,int prio) {
		super(name,prio);
	}
}



