package org.argouml.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileException;


public abstract class Profile {
	private Set<String>dependencies = new HashSet<String>();
	private Set<Critic>critics = new HashSet<Critic>();
	protected final void addProfileDependency(Profile p)throws IllegalArgumentException {
		addProfileDependency(p.getProfileIdentifier());
	}
	protected void addProfileDependency(String profileIdentifier) {
		dependencies.add(profileIdentifier);
	}
	public final Set<Profile>getDependencies() {
		if (ProfileFacade.isInitiated()) {
			Set<Profile>ret = new HashSet<Profile>();
			for (String pid:dependencies) {
				Profile p = ProfileFacade.getManager().lookForRegisteredProfile(pid);
				if (p != null) {
					ret.add(p);
					ret.addAll(p.getDependencies());
				}
			}
			return ret;
		}else {
			return new HashSet<Profile>();
		}
	}
	public final Set<String>getDependenciesID() {
		return dependencies;
	}
	public abstract String getDisplayName();
	public FormatingStrategy getFormatingStrategy() {
		return null;
	}
	public FigNodeStrategy getFigureStrategy() {
		return null;
	}
	public DefaultTypeStrategy getDefaultTypeStrategy() {
		return null;
	}
	public Collection getProfilePackages()throws ProfileException {
		return new ArrayList();
	}
	@Override public String toString() {
		return getDisplayName();
	}
	public Set<Critic>getCritics() {
		return critics;
	}
	public String getProfileIdentifier() {
		return getDisplayName();
	}
	protected void setCritics(Set<Critic>criticsSet) {
		this.critics = criticsSet;
	}
}



