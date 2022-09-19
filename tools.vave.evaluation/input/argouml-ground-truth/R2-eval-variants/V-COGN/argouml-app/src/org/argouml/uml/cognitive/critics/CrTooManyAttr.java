package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


public class CrTooManyAttr extends AbstractCrTooMany {
	private static final int ATTRIBUTES_THRESHOLD = 7;
	public CrTooManyAttr() {
		setupHeadAndDesc();
		addSupportedDecision(UMLDecision.STORAGE);
		setThreshold(ATTRIBUTES_THRESHOLD);
		addTrigger("structuralFeature");
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		if (!(Model.getFacade().isAClassifier(dm))) {
			return NO_PROBLEM;
		}
		Collection features = Model.getFacade().getFeatures(dm);
		if (features == null) {
			return NO_PROBLEM;
		}
		int n = 0;
		for (Iterator iter = features.iterator();iter.hasNext();) {
			if (Model.getFacade().isAStructuralFeature(iter.next())) {
				n++;
			}
		}
		if (n <= getThreshold()) {
			return NO_PROBLEM;
		}
		return PROBLEM_FOUND;
	}
	public Set<Object>getCriticizedDesignMaterials() {
		Set<Object>ret = new HashSet<Object>();
		ret.add(Model.getMetaTypes().getUMLClass());
		return ret;
	}
	private static final long serialVersionUID = 1281218975903539324l;
}



