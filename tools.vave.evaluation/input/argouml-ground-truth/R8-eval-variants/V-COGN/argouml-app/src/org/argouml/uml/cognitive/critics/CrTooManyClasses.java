package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrTooManyClasses extends AbstractCrTooMany {
	private static final int CLASS_THRESHOLD = 20;
	public CrTooManyClasses() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.CLASS_SELECTION);
		setThreshold(CLASS_THRESHOLD);
		addTrigger("ownedElement");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isANamespace(dm))) {
			return NO_PROBLEM;
		}
		Collection subs = Model.getFacade().getOwnedElements(dm);
		Collection<Object>classes = new ArrayList<Object>();
		for (Object me:subs) {
			if (Model.getFacade().isAClass(me)) {
				classes.add(me);
			}
		}
		if (classes.size() <= getThreshold()) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getNamespace());
		return ret;
	}
	private static final long serialVersionUID = -3270186791825482658l;
}



