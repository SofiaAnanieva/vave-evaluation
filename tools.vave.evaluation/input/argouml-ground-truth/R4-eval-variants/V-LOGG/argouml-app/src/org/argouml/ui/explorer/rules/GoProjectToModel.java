package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;


public class GoProjectToModel extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.project.model");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof Project) {
			return((Project) parent).getUserDefinedModelList();
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



