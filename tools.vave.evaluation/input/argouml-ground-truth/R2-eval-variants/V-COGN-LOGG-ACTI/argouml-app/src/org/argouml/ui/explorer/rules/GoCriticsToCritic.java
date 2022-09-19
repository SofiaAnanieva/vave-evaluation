package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;
import org.argouml.cognitive.CompoundCritic;
import org.argouml.cognitive.Critic;
import org.argouml.i18n.Translator;
import org.argouml.profile.Profile;


public class GoCriticsToCritic implements PerspectiveRule {
	public String getRuleName() {
		return Translator.localize("misc.profile.critic");
	}
	public Collection getChildren(final Object parent) {
		if (parent instanceof Collection) {
			Collection v = (Collection) parent;
			if (!v.isEmpty()) {
				if (v.iterator().next()instanceof Critic) {
					Vector<Object>ret = new Vector<Object>();
					for (Object critic:v) {
						final Critic fc = (Critic) critic;
						if (critic instanceof CompoundCritic) {
							Object compound = new Vector<Critic>() {
							 {
								addAll(((CompoundCritic) fc).getCriticList());
							}
							public String toString() {
								return Translator.localize("misc.profile.explorer.compound");
							}
						};
							ret.add(compound);
						}else {
							ret.add(critic);
						}
					}
					return ret;
				}else {
					return(Collection) parent;
				}
			}else {
				return Collections.EMPTY_SET;
			}
		}
		return Collections.EMPTY_SET;
	}
	public Set getDependencies(Object parent) {
		return Collections.EMPTY_SET;
	}
}



