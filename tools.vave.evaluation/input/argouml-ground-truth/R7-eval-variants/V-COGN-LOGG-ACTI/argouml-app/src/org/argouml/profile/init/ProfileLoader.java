package org.argouml.profile.init;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.i18n.Translator;
import org.argouml.moduleloader.ModuleLoader2;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.UserDefinedProfile;
import org.argouml.profile.ProfileManager;
import org.argouml.profile.ProfileFacade;


public final class ProfileLoader {
	private static final Logger LOG = Logger.getLogger(ProfileLoader.class);
	private static final String JAR_PREFIX = "jar:";
	private static final String FILE_PREFIX = "file:";
	public void doLoad() {
		List<String>extDirs = ModuleLoader2.getInstance().getExtensionLocations();
		for (String extDir:extDirs) {
			huntForProfilesInDir(extDir);
		}
	}
	private void huntForProfilesInDir(String dir) {
		LOG.info("Looking for Profiles in " + dir);
		File extensionDir = new File(dir);
		if (extensionDir.isDirectory()) {
			File[]files = extensionDir.listFiles(new JarFileFilter());
			for (File file:files) {
				JarFile jarfile = null;
				try {
					jarfile = new JarFile(file);
					if (jarfile != null) {
						LOG.info("Looking for Profiles in the Jar " + jarfile.getName());
						ClassLoader classloader = new URLClassLoader(new URL[] {file.toURI().toURL()});
						loadProfilesFromJarFile(jarfile.getManifest(),file,classloader);
					}
				}catch (IOException ioe) {
					LOG.debug("Cannot open Jar file " + file,ioe);
				}
			}
		}
	}
	private void loadProfilesFromJarFile(Manifest manifest,File file,ClassLoader classloader) {
		Map<String,Attributes>entries = manifest.getEntries();
		boolean classLoaderAlreadyAdded = false;
		for (String entryName:entries.keySet()) {
			Attributes attr = entries.get(entryName);
			if (new Boolean(attr.getValue("Profile") + "").booleanValue()) {
				try {
					if (!classLoaderAlreadyAdded) {
						Translator.addClassLoader(classloader);
						classLoaderAlreadyAdded = true;
					}
					Set<Critic>critics = loadJavaCriticsForProfile(attr,classloader);
					String modelPath = attr.getValue("Model");
					URL modelURL = null;
					if (modelPath != null) {
						modelURL = new URL(JAR_PREFIX + FILE_PREFIX + file.getCanonicalPath() + "!" + modelPath);
					}
					UserDefinedProfile udp = new UserDefinedProfile(entryName,modelURL,critics,loadManifestDependenciesForProfile(attr));
					ProfileFacade.getManager().registerProfile(udp);
					LOG.debug("Registered Profile: " + udp.getDisplayName() + "...");
				}catch (ProfileException e) {
					LOG.error("Exception",e);
				}catch (IOException e) {
					LOG.error("Exception",e);
				}
			}
		}
	}
	private Set<String>loadManifestDependenciesForProfile(Attributes attr) {
		Set<String>ret = new HashSet<String>();
		String value = attr.getValue("Depends-on");
		if (value != null) {
			StringTokenizer st = new StringTokenizer(value,",");
			while (st.hasMoreElements()) {
				String entry = st.nextToken().trim();
				ret.add(entry);
			}
		}
		return ret;
	}
	private Set<Critic>loadJavaCriticsForProfile(Attributes attr,ClassLoader classloader) {
		Set<Critic>ret = new HashSet<Critic>();
		String value = attr.getValue("Java-Critics");
		if (value != null) {
			StringTokenizer st = new StringTokenizer(value,",");
			while (st.hasMoreElements()) {
				String entry = st.nextToken().trim();
				try {
					Class cl = classloader.loadClass(entry);
					Critic critic = (Critic) cl.newInstance();
					ret.add(critic);
				}catch (ClassNotFoundException e) {
					LOG.error("Error loading class: " + entry,e);
				}catch (InstantiationException e) {
					LOG.error("Error instantianting class: " + entry,e);
				}catch (IllegalAccessException e) {
					LOG.error("Exception",e);
				}
			}
		}
		return ret;
	}
	static class JarFileFilter implements FileFilter {
	public boolean accept(File pathname) {
		return(pathname.canRead()&&pathname.isFile()&&pathname.getPath().toLowerCase().endsWith(".jar"));
	}
}
}


