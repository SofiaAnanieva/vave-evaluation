package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoPackageToElementImport extends AbstractPerspectiveRule {
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAPackage(parent)) {
			return Model.getFacade().getElementImports(parent);
		}
		return Collections.EMPTY_SET;
	}
	public String getRuleName() {
		return Translator.localize("misc.package.element-import");
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



