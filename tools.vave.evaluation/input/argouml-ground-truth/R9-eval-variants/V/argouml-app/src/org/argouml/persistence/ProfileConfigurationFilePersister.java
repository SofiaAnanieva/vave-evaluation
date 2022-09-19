package org.argouml.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.model.Model;
import org.argouml.model.UmlException;
import org.argouml.model.XmiWriter;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.UserDefinedProfile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.argouml.persistence.UmlFilePersister;
import org.argouml.persistence.MemberFilePersister;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.Profile;


public class ProfileConfigurationFilePersister extends MemberFilePersister {
	public String getMainTag() {
		return"profile";
	}
	public void load(Project project,InputStream inputStream)throws OpenException {
		load(project,new InputSource(inputStream));
	}
	public void load(Project project,InputSource inputSource)throws OpenException {
		try {
			ProfileConfigurationParser parser = new ProfileConfigurationParser();
			parser.parse(inputSource);
			Collection<Profile>profiles = parser.getProfiles();
			Collection<String>unresolved = parser.getUnresolvedFilenames();
			if (!unresolved.isEmpty()) {
				profiles.addAll(loadUnresolved(unresolved));
			}
			ProfileConfiguration pc = new ProfileConfiguration(project,profiles);
			project.setProfileConfiguration(pc);
		}catch (Exception e) {
			if (e instanceof OpenException) {
				throw(OpenException) e;
			}
			throw new OpenException(e);
		}
	}
	private Collection<Profile>loadUnresolved(Collection<String>unresolved) {
		Collection<Profile>profiles = new ArrayList<Profile>();
		ProfileManager profileManager = ProfileFacade.getManager();
		for (String filename:unresolved) {
		}
		return profiles;
	}
	private void addUserDefinedProfile(String fileName,StringBuffer xmi,ProfileManager profileManager)throws IOException {
		File profilesDirectory = getProfilesDirectory(profileManager);
		File profileFile = new File(profilesDirectory,fileName);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(profileFile),Argo.getEncoding());
		writer.write(xmi.toString());
		writer.close();
		if (isSomeProfileDirectoryConfigured(profileManager)) {
			profileManager.refreshRegisteredProfiles();
		}else {
			profileManager.addSearchPathDirectory(profilesDirectory.getAbsolutePath());
		}
	}
	private static File getProfilesDirectory(ProfileManager profileManager) {
		if (isSomeProfileDirectoryConfigured(profileManager)) {
			List<String>directories = profileManager.getSearchPathDirectories();
			return new File(directories.get(0));
		}else {
			File userSettingsFile = new File(Configuration.getFactory().getConfigurationHandler().getDefaultPath());
			return userSettingsFile.getParentFile();
		}
	}
	private static boolean isSomeProfileDirectoryConfigured(ProfileManager profileManager) {
		return profileManager.getSearchPathDirectories().size() > 0;
	}
	public void save(ProjectMember member,OutputStream stream)throws SaveException {
		PrintWriter w;
		try {
			w = new PrintWriter(new OutputStreamWriter(stream,"UTF-8"));
		}catch (UnsupportedEncodingException e1) {
			throw new SaveException("UTF-8 encoding not supported on platform",e1);
		}
		saveProjectMember(member,w);
		w.flush();
	}
	private void saveProjectMember(ProjectMember member,PrintWriter w)throws SaveException {
		try {
			if (member instanceof ProfileConfiguration) {
				ProfileConfiguration pc = (ProfileConfiguration) member;
				w.println("<?xml version = \"1.0\" encoding = \"UTF-8\" ?>");
				w.println("");
				w.println("<profile>");
				for (Profile profile:pc.getProfiles()) {
					if (profile instanceof UserDefinedProfile) {
						UserDefinedProfile uprofile = (UserDefinedProfile) profile;
						w.println("\t\t<userDefined>");
						w.println("\t\t\t<filename>" + uprofile.getModelFile().getName() + "</filename>");
						w.println("\t\t\t<model>");
						printModelXMI(w,uprofile.getProfilePackages());
						w.println("\t\t\t</model>");
						w.println("\t\t</userDefined>");
					}else {
						w.println("\t\t<plugin>");
						w.println("\t\t\t" + profile.getProfileIdentifier());
						w.println("\t\t</plugin>");
					}
				}
				w.println("</profile>");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SaveException(e);
		}
	}
	private void printModelXMI(PrintWriter w,Collection profileModels)throws UmlException {
		if (true) {
			return;
		}
		StringWriter myWriter = new StringWriter();
		for (Object model:profileModels) {
			XmiWriter xmiWriter = Model.getXmiWriter(model,(OutputStream) null,ApplicationVersion.getVersion() + "(" + UmlFilePersister.PERSISTENCE_VERSION + ")");
			xmiWriter.write();
		}
		myWriter.flush();
		w.println("" + myWriter.toString());
	}
	@Override public void load(Project project,URL url)throws OpenException {
		load(project,new InputSource(url.toExternalForm()));
	}
}

class ProfileConfigurationParser extends SAXParserBase {
	private ProfileConfigurationTokenTable tokens = new ProfileConfigurationTokenTable();
	private Profile profile;
	private String model;
	private String filename;
	private Collection<Profile>profiles = new ArrayList<Profile>();
	private Collection<String>unresolvedFilenames = new ArrayList<String>();
	public ProfileConfigurationParser() {
	}
	public Collection<Profile>getProfiles() {
		return profiles;
	}
	public Collection<String>getUnresolvedFilenames() {
		return unresolvedFilenames;
	}
	public void handleStartElement(XMLElement e) {
		try {
			switch (tokens.toToken(e.getName(),true)) {case ProfileConfigurationTokenTable.TOKEN_PROFILE:
				break;
			case ProfileConfigurationTokenTable.TOKEN_PLUGIN:
				profile = null;
				break;
			case ProfileConfigurationTokenTable.TOKEN_USER_DEFINED:
				profile = null;
				filename = null;
				model = null;
				break;
			case ProfileConfigurationTokenTable.TOKEN_FILENAME:
				break;
			case ProfileConfigurationTokenTable.TOKEN_MODEL:
				break;
			default:
				break;
			}
		}catch (Exception ex) {
		}
	}
	public void handleEndElement(XMLElement e)throws SAXException {
		try {
			switch (tokens.toToken(e.getName(),false)) {case ProfileConfigurationTokenTable.TOKEN_PROFILE:
				handleProfileEnd(e);
				break;
			case ProfileConfigurationTokenTable.TOKEN_PLUGIN:
				handlePluginEnd(e);
				break;
			case ProfileConfigurationTokenTable.TOKEN_USER_DEFINED:
				handleUserDefinedEnd(e);
				break;
			case ProfileConfigurationTokenTable.TOKEN_FILENAME:
				handleFilenameEnd(e);
				break;
			case ProfileConfigurationTokenTable.TOKEN_MODEL:
				handleModelEnd(e);
				break;
			default:
				break;
			}
		}catch (Exception ex) {
			throw new SAXException(ex);
		}
	}
	protected void handleProfileEnd(XMLElement e) {
	}
	protected void handlePluginEnd(XMLElement e)throws SAXException {
		String name = e.getText().trim();
		profile = lookupProfile(name);
		if (profile != null) {
			profiles.add(profile);
		}
	}
	private static Profile lookupProfile(String profileIdentifier)throws SAXException {
		Profile profile;
		profile = ProfileFacade.getManager().lookForRegisteredProfile(profileIdentifier);
		if (profile == null) {
			profile = ProfileFacade.getManager().getProfileForClass(profileIdentifier);
			if (profile == null) {
				throw new SAXException("Plugin profile \"" + profileIdentifier + "\" is not available in installation.",null);
			}
		}
		return profile;
	}
	protected void handleUserDefinedEnd(XMLElement e) {
		profile = getMatchingUserDefinedProfile(filename,ProfileFacade.getManager());
		if (profile == null) {
			unresolvedFilenames.add(filename);
		}else {
			profiles.add(profile);
		}
	}
	private static Profile getMatchingUserDefinedProfile(String fileName,ProfileManager profileManager) {
		for (Profile candidateProfile:profileManager.getRegisteredProfiles()) {
			if (candidateProfile instanceof UserDefinedProfile) {
				UserDefinedProfile userProfile = (UserDefinedProfile) candidateProfile;
				if (userProfile.getModelFile() != null&&fileName.equals(userProfile.getModelFile().getName())) {
					return userProfile;
				}
			}
		}
		return null;
	}
	protected void handleFilenameEnd(XMLElement e) {
		filename = e.getText().trim();
	}
	protected void handleModelEnd(XMLElement e) {
		model = e.getText().trim();
	}
	class ProfileConfigurationTokenTable extends XMLTokenTableBase {
		private static final String STRING_PROFILE = "profile";
		private static final String STRING_PLUGIN = "plugin";
		private static final String STRING_USER_DEFINED = "userDefined";
		private static final String STRING_FILENAME = "filename";
		private static final String STRING_MODEL = "model";
		public static final int TOKEN_PROFILE = 1;
		public static final int TOKEN_PLUGIN = 2;
		public static final int TOKEN_USER_DEFINED = 3;
		public static final int TOKEN_FILENAME = 4;
		public static final int TOKEN_MODEL = 5;
		private static final int TOKEN_LAST = 5;
		public static final int TOKEN_UNDEFINED = 999;
		public ProfileConfigurationTokenTable() {
			super(TOKEN_LAST);
		}
		protected void setupTokens() {
			addToken(STRING_PROFILE,Integer.valueOf(TOKEN_PROFILE));
			addToken(STRING_PLUGIN,Integer.valueOf(TOKEN_PLUGIN));
			addToken(STRING_USER_DEFINED,Integer.valueOf(TOKEN_USER_DEFINED));
			addToken(STRING_FILENAME,Integer.valueOf(TOKEN_FILENAME));
			addToken(STRING_MODEL,Integer.valueOf(TOKEN_MODEL));
		}
	}
}



