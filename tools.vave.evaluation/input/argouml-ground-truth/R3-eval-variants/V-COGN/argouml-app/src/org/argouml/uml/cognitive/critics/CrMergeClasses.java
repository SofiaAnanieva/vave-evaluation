package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrMergeClasses extends CrUML {
	public CrMergeClasses() {
		setupHeadAndDesc();
		setPriority(ToDoItem.LOW_PRIORITY);
		addSupportedDecision(UMLDecision.CLASS_SELECTION);
		addTrigger("associationEnd");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
			return NO_PROBLEM;
		}
		Object cls = dm;
		Collection ends = Model.getFacade().getAssociationEnds(cls);
		if (ends == null||ends.size() != 1) {
			return NO_PROBLEM;
		}
		Object myEnd = ends.iterator().next();
		Object asc = Model.getFacade().getAssociation(myEnd);
		List conns = new ArrayList(Model.getFacade().getConnections(asc));
		if (conns == null||conns.size() != 2) {
			return NO_PROBLEM;
		}
		Object ae0 = conns.get(0);
		Object ae1 = conns.get(1);
		if (!(Model.getFacade().isAClass(Model.getFacade().getType(ae0))&&Model.getFacade().isAClass(Model.getFacade().getType(ae1)))) {
			return NO_PROBLEM;
		}
		if (!(Model.getFacade().isNavigable(ae0)&&Model.getFacade().isNavigable(ae1))) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().getLower(ae0) == 1&&Model.getFacade().getUpper(ae0) == 1&&Model.getFacade().getLower(ae1) == 1&&Model.getFacade().getUpper(ae1) == 1) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
}



