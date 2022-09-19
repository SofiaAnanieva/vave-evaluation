package org.argouml.profile.internal;

import java.util.Collection;
import org.argouml.model.Model;


public class ModelUtils {
	public static Object findTypeInModel(String s,Object model) {
		if (!Model.getFacade().isANamespace(model)) {
			throw new IllegalArgumentException("Looking for the classifier " + s + " in a non-namespace object of " + model + ". A namespace was expected.");
		}
		Collection allClassifiers = Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getClassifier());
		for (Object classifier:allClassifiers) {
			if (Model.getFacade().getName(classifier) != null&&Model.getFacade().getName(classifier).equals(s)) {
				return classifier;
			}
		}
		return null;
	}
}



