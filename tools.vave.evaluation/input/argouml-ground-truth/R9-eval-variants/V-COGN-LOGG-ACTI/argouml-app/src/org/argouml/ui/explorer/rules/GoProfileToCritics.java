package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.i18n.Translator;
import org.argouml.profile.Profile;


public class GoProfileToCritics extends AbstractPerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.profile.critics");
	}
	public Collection getChildren(final Object parent) {
		if (parent instanceof Profile) {
			Object critics = new ArrayList<Critic>() {
			 {
				addAll(((Profile) parent).getCritics());
			}
			@Override public String toString() {
				return Translator.localize("misc.profile.explorer.critic");
			}
		};
			Collection ret = new ArrayList<Object>();
			ret.add(critics);
			return ret;
		}
		return Collections.emptySet();
	}
	public Set getDependencies(Object parent) {
		return Collections.emptySet();
	}
}



