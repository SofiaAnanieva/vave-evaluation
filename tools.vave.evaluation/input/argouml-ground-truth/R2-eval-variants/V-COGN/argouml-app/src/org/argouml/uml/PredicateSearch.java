package org.argouml.uml;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.Predicate;
import org.argouml.util.PredicateTrue;


public class PredicateSearch implements Predicate {
	private Predicate elementName;
	private Predicate packageName;
	private Predicate diagramName;
	private Predicate theType;
	private Predicate specific = PredicateTrue.getInstance();
	public PredicateSearch(Predicate elementNamePredicate,Predicate packageNamePredicate,Predicate diagramNamePredicate,Predicate typePredicate) {
		elementName = elementNamePredicate;
		packageName = packageNamePredicate;
		diagramName = diagramNamePredicate;
		theType = typePredicate;
	}
	public boolean matchDiagram(ArgoDiagram diagram) {
		return matchDiagram(diagram.getName());
	}
	public boolean matchDiagram(String name) {
		return diagramName.evaluate(name);
	}
	public boolean matchPackage(Object pkg) {
		boolean res = packageName.evaluate(Model.getFacade().getName(pkg));
		return res;
	}
	public boolean evaluate(Object element) {
		if (!(Model.getFacade().isAUMLElement(element))) {
			return false;
		}
		Object me = element;
		return theType.evaluate(me)&&specific.evaluate(me)&&elementName.evaluate(Model.getFacade().getName(me));
	}
}



