package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;


public class GoProjectToProfileConfiguration extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.project.profileconfiguration");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof Project) {
			Collection l = new ArrayList();
			l.add(((Project) parent).getProfileConfiguration());
			return l;
		}
		return Collections.EMPTY_LIST;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



