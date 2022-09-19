package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;


public class GoProjectToRoots extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.project.roots");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof Project) {
			return((Project) parent).getRoots();
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



