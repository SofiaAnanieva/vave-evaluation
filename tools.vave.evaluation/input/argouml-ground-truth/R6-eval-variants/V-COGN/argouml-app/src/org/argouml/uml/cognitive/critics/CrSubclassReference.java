package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.GenDescendantClasses;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;


public class CrSubclassReference extends CrUML {
	public CrSubclassReference() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
		setKnowledgeTypes(Critic.KT_SEMANTICS);
		addTrigger("specialization");
		addTrigger("associationEnd");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClass(dm))) {
			return NO_PROBLEM;
		}
		Object cls = dm;
		ListSet offs = computeOffenders(cls);
		if (offs != null) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		Object cls = dm;
		ListSet offs = computeOffenders(cls);
		return new UMLToDoItem(this,offs,dsgr);
	}
	@Override public boolean stillValid(ToDoItem i,Designer dsgr) {
		if (!isActive()) {
			return false;
		}
		ListSet offs = i.getOffenders();
		Object dm = offs.get(0);
		ListSet newOffs = computeOffenders(dm);
		boolean res = offs.equals(newOffs);
		return res;
	}
	public ListSet computeOffenders(Object cls) {
		Collection asc = Model.getFacade().getAssociationEnds(cls);
		if (asc == null||asc.size() == 0)return null;
		Enumeration descendEnum = GenDescendantClasses.getSINGLETON().gen(cls);
		if (!descendEnum.hasMoreElements()) {
			return null;
		}
		ListSet descendants = new ListSet();
		while (descendEnum.hasMoreElements())descendants.add(descendEnum.nextElement());
		ListSet offs = null;
		for (Object ae:asc) {
			Object a = Model.getFacade().getAssociation(ae);
			List conn = new ArrayList(Model.getFacade().getConnections(a));
			if (conn.size() != 2) {
				continue;
			}
			Object otherEnd = conn.get(0);
			if (ae == conn.get(0))otherEnd = conn.get(1);
			if (!Model.getFacade().isNavigable(otherEnd)) {
				continue;
			}
			Object otherCls = Model.getFacade().getType(otherEnd);
			if (descendants.contains(otherCls)) {
				if (offs == null) {
					offs = new ListSet();
					offs.add(cls);
				}
				offs.add(a);
				offs.add(otherCls);
			}
		}
		return offs;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
}



