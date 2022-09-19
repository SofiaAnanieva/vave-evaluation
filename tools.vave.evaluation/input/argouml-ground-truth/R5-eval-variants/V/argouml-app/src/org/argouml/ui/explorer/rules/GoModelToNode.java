package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class GoModelToNode extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.model.node");
	}
	public Collection getChildren(Object parent) {
		if (Model.getFacade().isAModel(parent)) {
			return Model.getModelManagementHelper().getAllModelElementsOfKind(parent,Model.getMetaTypes().getNode());
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



