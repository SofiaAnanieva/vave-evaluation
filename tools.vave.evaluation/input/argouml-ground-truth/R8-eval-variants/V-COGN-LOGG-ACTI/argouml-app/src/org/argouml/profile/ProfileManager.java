package org.argouml.profile;

import java.util.List;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.profile.Profile;


public interface ProfileManager {
	void registerProfile(Profile profile);
	void removeProfile(Profile profile);
	List<Profile>getRegisteredProfiles();
	Profile getProfileForClass(String className);
	List<Profile>getDefaultProfiles();
	void addToDefaultProfiles(Profile profile);
	void removeFromDefaultProfiles(Profile profile);
	void addSearchPathDirectory(String path);
	void removeSearchPathDirectory(String path);
	List<String>getSearchPathDirectories();
	void refreshRegisteredProfiles();
	Profile getUMLProfile();
	Profile lookForRegisteredProfile(String profile);
	void applyConfiguration(ProfileConfiguration pc);
}



