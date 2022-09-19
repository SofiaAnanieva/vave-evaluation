package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;


public class GoProfileToModel extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.profile.model");
	}
	public Collection getChildren(Object parent) {
		if (parent instanceof Profile) {
			try {
				Collection col = ((Profile) parent).getProfilePackages();
				return col;
			}catch (ProfileException e) {
				return Collections.EMPTY_SET;
			}
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



