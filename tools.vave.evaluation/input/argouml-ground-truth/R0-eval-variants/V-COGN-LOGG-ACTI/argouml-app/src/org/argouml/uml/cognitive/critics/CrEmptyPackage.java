package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrEmptyPackage extends CrUML {
	private static final Logger LOG = Logger.getLogger(CrEmptyPackage.class);
	public CrEmptyPackage() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.MODULARITY);
		addTrigger("ownedElement");
	}
	@Override public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAPackage(dm))) {
			return NO_PROBLEM;
		}
		Collection elems = Model.getFacade().getOwnedElements(dm);
		if (elems.size() == 0) {
			return PROBLEM_FOUND;
		}
		return NO_PROBLEM;
	}
	@Override public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getPackage());
		return ret;
	}
}



