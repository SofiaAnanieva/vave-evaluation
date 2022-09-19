package org.argouml.profile;

import org.argouml.kernel.ProfileConfiguration;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileManager;


public class ProfileFacade {
	public static void register(Profile profile) {
		getManager().registerProfile(profile);
	}
	public static void remove(Profile profile) {
		getManager().removeProfile(profile);
	}
	public static ProfileManager getManager() {
		if (manager == null) {
			notInitialized("manager");
		}
		return manager;
	}
	private static void notInitialized(String string) {
		throw new RuntimeException("ProfileFacade\'s " + string + " isn\'t initialized!");
	}
	public static void setManager(ProfileManager profileManager) {
		manager = profileManager;
	}
	static void reset() {
		manager = null;
	}
	private static ProfileManager manager;
	public static boolean isInitiated() {
		return manager != null;
	}
	public static void applyConfiguration(ProfileConfiguration pc) {
		getManager().applyConfiguration(pc);
	}
}



