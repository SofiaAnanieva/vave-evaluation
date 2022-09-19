package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.UMLToDoItem;


public class CrAssocNameConflict extends CrUML {
	public CrAssocNameConflict() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		return computeOffenders(dm).size() > 1;
	}
	public ToDoItem toDoItem(Object dm,Designer dsgr) {
		ListSet offs = computeOffenders(dm);
		return new UMLToDoItem(this,offs,dsgr);
	}
	protected ListSet computeOffenders(Object dm) {
		ListSet offenderResult = new ListSet();
		if (Model.getFacade().isANamespace(dm)) {
			HashMap<String,Object>names = new HashMap<String,Object>();
			for (Object name1Object:Model.getFacade().getOwnedElements(dm)) {
				if (!Model.getFacade().isAAssociation(name1Object)) {
					continue;
				}
				String name = Model.getFacade().getName(name1Object);
				Collection typ1 = getAllTypes(name1Object);
				if (name == null||"".equals(name)) {
					continue;
				}
				if (names.containsKey(name)) {
					Object offender = names.get(name);
					Collection typ2 = getAllTypes(offender);
					if (typ1.containsAll(typ2)&&typ2.containsAll(typ1)) {
						if (!offenderResult.contains(offender)) {
							offenderResult.add(offender);
						}
						offenderResult.add(name1Object);
					}
				}
				names.put(name,name1Object);
			}
		}
		return offenderResult;
	}
	@Override public boolean stillValid(ToDoItem i,Designer dsgr) {
		if (!isActive()) {
			return false;
		}
		ListSet offs = i.getOffenders();
		Object f = offs.get(0);
		Object ns = Model.getFacade().getNamespace(f);
		if (!predicate(ns,dsgr)) {
			return false;
		}
		ListSet newOffs = computeOffenders(ns);
		boolean res = offs.equals(newOffs);
		return res;
	}
	public Collection getAllTypes(Object assoc) {
		Set list = new HashSet();
		if (assoc == null) {
			return list;
		}
		Collection assocEnds = Model.getFacade().getConnections(assoc);
		if (assocEnds == null) {
			return list;
		}
		for (Object element:assocEnds) {
			if (Model.getFacade().isAAssociationEnd(element)) {
				Object type = Model.getFacade().getType(element);
				list.add(type);
			}
		}
		return list;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getNamespace());
		return ret;
	}
}



