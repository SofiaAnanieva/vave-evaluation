package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoPackageToClass extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.package.class");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAPackage(parent)) {
			return Model.getModelManagementHelper().getAllModelElementsOfKind(parent,Model.getMetaTypes().getUMLClass());
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



