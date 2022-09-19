package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrDupRoleNames extends CrUML {
	public CrDupRoleNames() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.NAMING);
		addTrigger("connection");
		addTrigger("end_name");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAAssociation(dm))) {
			return NO_PROBLEM;
		}
		if (Model.getFacade().isAAssociationRole(dm)) {
			return NO_PROBLEM;
		}
		Collection<String>namesSeen = new ArrayList<String>();
		Iterator conns = Model.getFacade().getConnections(dm).iterator();
		while (conns.hasNext()) {
			String name = Model.getFacade().getName(conns.next());
			if ((name == null)||name.equals("")) {
				continue;
			}
			if (namesSeen.contains(name)) {
				return PROBLEM_FOUND;
			}
			namesSeen.add(name);
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getAssociationClass());
		return ret;
	}
}



