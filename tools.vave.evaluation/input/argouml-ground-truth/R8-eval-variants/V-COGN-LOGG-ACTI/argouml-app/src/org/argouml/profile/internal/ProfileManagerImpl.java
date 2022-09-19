package org.argouml.profile.internal;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Agency;
import org.argouml.cognitive.Critic;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.UserDefinedProfileHelper;
import org.argouml.uml.cognitive.critics.ProfileCodeGeneration;
import org.argouml.uml.cognitive.critics.ProfileGoodPractices;
import org.argouml.profile.ProfileManager;


public class ProfileManagerImpl implements ProfileManager {
	private static final Logger LOG = Logger.getLogger(ProfileManagerImpl.class);
	private static final String DIRECTORY_SEPARATOR = "*";
	public static final ConfigurationKey KEY_DEFAULT_PROFILES = Configuration.makeKey("profiles","default");
	public static final ConfigurationKey KEY_DEFAULT_DIRECTORIES = Configuration.makeKey("profiles","directories");
	private boolean disableConfigurationUpdate = false;
	private List<Profile>profiles = new ArrayList<Profile>();
	private List<Profile>defaultProfiles = new ArrayList<Profile>();
	private List<String>searchDirectories = new ArrayList<String>();
	private ProfileUML profileUML;
	private ProfileJava profileJava;
	private ProfileGoodPractices profileGoodPractices;
	private ProfileCodeGeneration profileCodeGeneration;
	public ProfileManagerImpl() {
		try {
			disableConfigurationUpdate = true;
			profileUML = new ProfileUML();
			profileJava = new ProfileJava(profileUML);
			profileGoodPractices = new ProfileGoodPractices();
			profileCodeGeneration = new ProfileCodeGeneration(profileGoodPractices);
			registerProfile(profileUML);
			addToDefaultProfiles(profileUML);
			registerProfile(profileJava);
			registerProfile(profileGoodPractices);
			registerProfile(profileCodeGeneration);
			registerProfile(new ProfileMeta());
		}catch (ProfileException e) {
			throw new RuntimeException(e);
		}finally {
			disableConfigurationUpdate = false;
		}
		loadDirectoriesFromConfiguration();
		refreshRegisteredProfiles();
		loadDefaultProfilesfromConfiguration();
	}
	private void loadDefaultProfilesfromConfiguration() {
		if (!disableConfigurationUpdate) {
			disableConfigurationUpdate = true;
			String defaultProfilesList = Configuration.getString(KEY_DEFAULT_PROFILES);
			if (defaultProfilesList.equals("")) {
				addToDefaultProfiles(profileJava);
				addToDefaultProfiles(profileGoodPractices);
				addToDefaultProfiles(profileCodeGeneration);
			}else {
				StringTokenizer tokenizer = new StringTokenizer(defaultProfilesList,DIRECTORY_SEPARATOR,false);
				while (tokenizer.hasMoreTokens()) {
					String desc = tokenizer.nextToken();
					Profile p = null;
					if (desc.charAt(0) == 'U') {
						String fileName = desc.substring(1);
						File file;
						try {
							file = new File(new URI(fileName));
							p = findUserDefinedProfile(file);
							if (p == null) {
								try {
									p = new UserDefinedProfile(file);
									registerProfile(p);
								}catch (ProfileException e) {
									LOG.error("Error loading profile: " + file,e);
								}
							}
						}catch (URISyntaxException e1) {
							LOG.error("Invalid path for Profile: " + fileName,e1);
						}catch (Throwable e2) {
							LOG.error("Error loading profile: " + fileName,e2);
						}
					}else if (desc.charAt(0) == 'C') {
						String profileIdentifier = desc.substring(1);
						p = lookForRegisteredProfile(profileIdentifier);
					}
					if (p != null) {
						addToDefaultProfiles(p);
					}
				}
			}
			disableConfigurationUpdate = false;
		}
	}
	private void updateDefaultProfilesConfiguration() {
		if (!disableConfigurationUpdate) {
			StringBuffer buf = new StringBuffer();
			for (Profile p:defaultProfiles) {
				if (p instanceof UserDefinedProfile) {
					buf.append("U" + ((UserDefinedProfile) p).getModelFile().toURI().toASCIIString());
				}else {
					buf.append("C" + p.getProfileIdentifier());
				}
				buf.append(DIRECTORY_SEPARATOR);
			}
			Configuration.setString(KEY_DEFAULT_PROFILES,buf.toString());
		}
	}
	private void loadDirectoriesFromConfiguration() {
		disableConfigurationUpdate = true;
		StringTokenizer tokenizer = new StringTokenizer(Configuration.getString(KEY_DEFAULT_DIRECTORIES),DIRECTORY_SEPARATOR,false);
		while (tokenizer.hasMoreTokens()) {
			searchDirectories.add(tokenizer.nextToken());
		}
		disableConfigurationUpdate = false;
	}
	private void updateSearchDirectoriesConfiguration() {
		if (!disableConfigurationUpdate) {
			StringBuffer buf = new StringBuffer();
			for (String s:searchDirectories) {
				buf.append(s).append(DIRECTORY_SEPARATOR);
			}
			Configuration.setString(KEY_DEFAULT_DIRECTORIES,buf.toString());
		}
	}
	public List<Profile>getRegisteredProfiles() {
		return profiles;
	}
	public void registerProfile(Profile p) {
		if (p != null&&!profiles.contains(p)) {
			if (p instanceof UserDefinedProfile||getProfileForClass(p.getClass().getName()) == null) {
				profiles.add(p);
				for (Critic critic:p.getCritics()) {
					for (Object meta:critic.getCriticizedDesignMaterials()) {
						Agency.register(critic,meta);
					}
					critic.setEnabled(false);
				}
				loadDefaultProfilesfromConfiguration();
			}
		}
	}
	public void removeProfile(Profile p) {
		if (p != null&&p != profileUML) {
			profiles.remove(p);
			defaultProfiles.remove(p);
		}
		try {
			Collection packages = p.getProfilePackages();
			if (packages != null&&!packages.isEmpty()) {
				Model.getUmlFactory().deleteExtent(packages.iterator().next());
			}
		}catch (ProfileException e) {
		}
	}
	private static final String OLD_PROFILE_PACKAGE = "org.argouml.uml.profile";
	private static final String NEW_PROFILE_PACKAGE = "org.argouml.profile.internal";
	public Profile getProfileForClass(String profileClass) {
		Profile found = null;
		if (profileClass != null&&profileClass.startsWith(OLD_PROFILE_PACKAGE)) {
			profileClass = profileClass.replace(OLD_PROFILE_PACKAGE,NEW_PROFILE_PACKAGE);
		}
		assert profileUML.getClass().getName().startsWith(NEW_PROFILE_PACKAGE);
		for (Profile p:profiles) {
			if (p.getClass().getName().equals(profileClass)) {
				found = p;
				break;
			}
		}
		return found;
	}
	public void addToDefaultProfiles(Profile p) {
		if (p != null&&profiles.contains(p)&&!defaultProfiles.contains(p)) {
			defaultProfiles.add(p);
			updateDefaultProfilesConfiguration();
		}
	}
	public List<Profile>getDefaultProfiles() {
		return Collections.unmodifiableList(defaultProfiles);
	}
	public void removeFromDefaultProfiles(Profile p) {
		if (p != null&&p != profileUML&&profiles.contains(p)) {
			defaultProfiles.remove(p);
			updateDefaultProfilesConfiguration();
		}
	}
	public void addSearchPathDirectory(String path) {
		if (path != null&&!searchDirectories.contains(path)) {
			searchDirectories.add(path);
			updateSearchDirectoriesConfiguration();
			try {
				Model.getXmiReader().addSearchPath(path);
			}catch (UmlException e) {
				LOG.error("Couldn\'t retrive XMI Reader from Model.",e);
			}
		}
	}
	public List<String>getSearchPathDirectories() {
		return Collections.unmodifiableList(searchDirectories);
	}
	public void removeSearchPathDirectory(String path) {
		if (path != null) {
			searchDirectories.remove(path);
			updateSearchDirectoriesConfiguration();
			try {
				Model.getXmiReader().removeSearchPath(path);
			}catch (UmlException e) {
				LOG.error("Couldn\'t retrive XMI Reader from Model.",e);
			}
		}
	}
	public void refreshRegisteredProfiles() {
		ArrayList<File>dirs = new ArrayList<File>();
		for (String dirName:searchDirectories) {
			File dir = new File(dirName);
			if (dir.exists()) {
				dirs.add(dir);
			}
		}
		if (!dirs.isEmpty()) {
			File[]fileArray = new File[dirs.size()];
			for (int i = 0;i < dirs.size();i++) {
				fileArray[i] = dirs.get(i);
			}
			List<File>dirList = UserDefinedProfileHelper.getFileList(fileArray);
			for (File file:dirList) {
				boolean found = findUserDefinedProfile(file) != null;
				if (!found) {
					UserDefinedProfile udp = null;
					try {
						udp = new UserDefinedProfile(file);
						registerProfile(udp);
					}catch (ProfileException e) {
						LOG.warn("Failed to load user defined profile " + file.getAbsolutePath() + ".",e);
					}
				}
			}
		}
	}
	private Profile findUserDefinedProfile(File file) {
		for (Profile p:profiles) {
			if (p instanceof UserDefinedProfile) {
				UserDefinedProfile udp = (UserDefinedProfile) p;
				if (file.equals(udp.getModelFile())) {
					return udp;
				}
			}
		}
		return null;
	}
	public Profile getUMLProfile() {
		return profileUML;
	}
	public Profile lookForRegisteredProfile(String value) {
		List<Profile>registeredProfiles = getRegisteredProfiles();
		for (Profile profile:registeredProfiles) {
			if (profile.getProfileIdentifier().equalsIgnoreCase(value)) {
				return profile;
			}
		}
		return null;
	}
	public void applyConfiguration(ProfileConfiguration pc) {
		for (Profile p:this.profiles) {
			for (Critic c:p.getCritics()) {
				c.setEnabled(false);
				Configuration.setBoolean(c.getCriticKey(),false);
			}
		}
		for (Profile p:pc.getProfiles()) {
			for (Critic c:p.getCritics()) {
				c.setEnabled(true);
				Configuration.setBoolean(c.getCriticKey(),true);
			}
		}
	}
}



