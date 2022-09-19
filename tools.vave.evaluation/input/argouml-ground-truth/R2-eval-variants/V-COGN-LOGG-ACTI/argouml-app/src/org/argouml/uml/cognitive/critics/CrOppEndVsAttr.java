package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrOppEndVsAttr extends CrUML {
	public CrOppEndVsAttr() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.INHERITANCE);
		addSupportedDecision(UMLDecision.RELATIONSHIPS);
		addSupportedDecision(UMLDecision.NAMING);
		setKnowledgeTypes(Critic.KT_SYNTAX);
		addTrigger("associationEnd");
		addTrigger("structuralFeature");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm))) {
			return NO_PROBLEM;
		}
		Object cls = dm;
		Collection<String>namesSeen = new ArrayList<String>();
		Collection str = Model.getFacade().getFeatures(cls);
		Iterator features = str.iterator();
		while (features.hasNext()) {
			Object o = features.next();
			if (!(Model.getFacade().isAStructuralFeature(o))) {
				continue;
			}
			Object sf = o;
			String sfName = Model.getFacade().getName(sf);
			if ("".equals(sfName)) {
				continue;
			}
			String nameStr = sfName;
			if (nameStr.length() == 0) {
				continue;
			}
			namesSeen.add(nameStr);
		}
		Collection assocEnds = Model.getFacade().getAssociationEnds(cls);
		Iterator myEnds = assocEnds.iterator();
		while (myEnds.hasNext()) {
			Object myAe = myEnds.next();
			Object asc = Model.getFacade().getAssociation(myAe);
			Collection conn = Model.getFacade().getConnections(asc);
			if (Model.getFacade().isAAssociationRole(asc)) {
				conn = Model.getFacade().getConnections(asc);
			}
			if (conn == null) {
				continue;
			}
			Iterator ascEnds = conn.iterator();
			while (ascEnds.hasNext()) {
				Object ae = ascEnds.next();
				if (Model.getFacade().getType(ae) == cls) {
					continue;
				}
				String aeName = Model.getFacade().getName(ae);
				if ("".equals(aeName)) {
					continue;
				}
				String aeNameStr = aeName;
				if (aeNameStr == null||aeNameStr.length() == 0) {
					continue;
				}
				if (namesSeen.contains(aeNameStr)) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getClassifier());
		return ret;
	}
	private static final long serialVersionUID = 5784567698177480475l;
}



