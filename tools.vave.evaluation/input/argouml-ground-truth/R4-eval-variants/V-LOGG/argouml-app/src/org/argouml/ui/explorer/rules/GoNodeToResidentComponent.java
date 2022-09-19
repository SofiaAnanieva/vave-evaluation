package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoNodeToResidentComponent extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.node.resident.component");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isANode(parent)) {
			return Model.getFacade().getDeployedComponents(parent);
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



