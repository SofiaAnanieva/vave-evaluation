package org.argouml.moduleloader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.Argo;
import org.argouml.i18n.Translator;


public final class ModuleLoader2 {
	private Map<ModuleInterface,ModuleStatus>moduleStatus;
	private List<String>extensionLocations = new ArrayList<String>();
	private static final ModuleLoader2 INSTANCE = new ModuleLoader2();
	private static final String FILE_PREFIX = "file:";
	private static final String JAR_PREFIX = "jar:";
	public static final String CLASS_SUFFIX = ".class";
	private ModuleLoader2() {
		moduleStatus = new HashMap<ModuleInterface,ModuleStatus>();
		computeExtensionLocations();
	}
	public static ModuleLoader2 getInstance() {
		return INSTANCE;
	}
	List<AbstractArgoJPanel>getDetailsTabs() {
		List<AbstractArgoJPanel>result = new ArrayList<AbstractArgoJPanel>();
		for (ModuleInterface module:getInstance().availableModules()) {
			ModuleStatus status = moduleStatus.get(module);
			if (status == null) {
				continue;
			}
			if (status.isEnabled()) {
				if (module instanceof DetailsTabProvider) {
					result.addAll(((DetailsTabProvider) module).getDetailsTabs());
				}
			}
		}
		return result;
	}
	private Collection<ModuleInterface>availableModules() {
		return Collections.unmodifiableCollection(moduleStatus.keySet());
	}
	public static void doLoad(boolean failingAllowed) {
		getInstance().doInternal(failingAllowed);
	}
	public static boolean isEnabled(String name) {
		return getInstance().isEnabledInternal(name);
	}
	public static Collection<String>allModules() {
		Collection<String>coll = new HashSet<String>();
		for (ModuleInterface mf:getInstance().availableModules()) {
			coll.add(mf.getName());
		}
		return coll;
	}
	public static boolean isSelected(String name) {
		return getInstance().isSelectedInternal(name);
	}
	private boolean isSelectedInternal(String name) {
		Map.
				Entry<ModuleInterface,ModuleStatus>entry = findModule(name);
		if (entry != null) {
			ModuleStatus status = entry.getValue();
			if (status == null) {
				return false;
			}
			return status.isSelected();
		}
		return false;
	}
	public static void setSelected(String name,boolean value) {
		getInstance().setSelectedInternal(name,value);
	}
	private void setSelectedInternal(String name,boolean value) {
		Map.
				Entry<ModuleInterface,ModuleStatus>entry = findModule(name);
		if (entry != null) {
			ModuleStatus status = entry.getValue();
			status.setSelected(value);
		}
	}
	public static String getDescription(String name) {
		return getInstance().getDescriptionInternal(name);
	}
	private String getDescriptionInternal(String name) {
		Map.
				Entry<ModuleInterface,ModuleStatus>entry = findModule(name);
		if (entry == null) {
			throw new IllegalArgumentException("Module does not exist.");
		}
		ModuleInterface module = entry.getKey();
		StringBuffer sb = new StringBuffer();
		String desc = module.getInfo(ModuleInterface.DESCRIPTION);
		if (desc != null) {
			sb.append(desc);
			sb.append("\n\n");
		}
		String author = module.getInfo(ModuleInterface.AUTHOR);
		if (author != null) {
			sb.append("Author: ").append(author);
			sb.append("\n");
		}
		String version = module.getInfo(ModuleInterface.VERSION);
		if (version != null) {
			sb.append("Version: ").append(version);
			sb.append("\n");
		}
		return sb.toString();
	}
	private void doInternal(boolean failingAllowed) {
		huntForModules();
		boolean someModuleSucceeded;
		do {
			someModuleSucceeded = false;
			for (ModuleInterface module:getInstance().availableModules()) {
				ModuleStatus status = moduleStatus.get(module);
				if (status == null) {
					continue;
				}
				if (!status.isEnabled()&&status.isSelected()) {
					try {
						if (module.enable()) {
							someModuleSucceeded = true;
							status.setEnabled();
						}
					}catch (Throwable e) {
					}
				}else if (status.isEnabled()&&!status.isSelected()) {
					try {
						if (module.disable()) {
							someModuleSucceeded = true;
							status.setDisabled();
						}
					}catch (Throwable e) {
					}
				}
			}
		}while (someModuleSucceeded);
		if (!failingAllowed) {
			for (ModuleInterface module:getInstance().availableModules()) {
				ModuleStatus status = moduleStatus.get(module);
				if (status == null) {
					continue;
				}
				if (status.isEnabled()&&status.isSelected()) {
					continue;
				}
				if (!status.isEnabled()&&!status.isSelected()) {
					continue;
				}
			}
		}
	}
	private boolean isEnabledInternal(String name) {
		Map.
				Entry<ModuleInterface,ModuleStatus>entry = findModule(name);
		if (entry != null) {
			ModuleStatus status = entry.getValue();
			if (status == null) {
				return false;
			}
			return status.isEnabled();
		}
		return false;
	}
	private Map.Entry<ModuleInterface,ModuleStatus>findModule(String name) {
		for (Map.Entry<ModuleInterface,ModuleStatus>entry:moduleStatus.entrySet()) {
			ModuleInterface module = entry.getKey();
			if (name.equalsIgnoreCase(module.getName())) {
				return entry;
			}
		}
		return null;
	}
	private void huntForModules() {
	}
	private void huntForModulesFromExtensionDir() {
		for (String location:extensionLocations) {
			huntModulesFromNamedDirectory(location);
		}
	}
	private void computeExtensionLocations() {
		String extForm = getClass().getResource(Argo.ARGOINI).toExternalForm();
		String argoRoot = extForm.substring(0,extForm.length() - Argo.ARGOINI.length());
		if (argoRoot.startsWith(JAR_PREFIX)) {
			argoRoot = argoRoot.substring(JAR_PREFIX.length());
			if (argoRoot.endsWith("!")) {
				argoRoot = argoRoot.substring(0,argoRoot.length() - 1);
			}
		}
		String argoHome = null;
		if (argoRoot != null) {
			if (argoRoot.startsWith(FILE_PREFIX)) {
				argoHome = new File(argoRoot.substring(FILE_PREFIX.length())).getAbsoluteFile().getParent();
			}else {
				argoHome = new File(argoRoot).getAbsoluteFile().getParent();
			}
			try {
				argoHome = java.net.URLDecoder.decode(argoHome,Argo.getEncoding());
			}catch (UnsupportedEncodingException e) {
			}
		}
		if (argoHome != null) {
			String extdir;
			if (argoHome.startsWith(FILE_PREFIX)) {
				extdir = argoHome.substring(FILE_PREFIX.length()) + File.separator + "ext";
			}else {
				extdir = argoHome + File.separator + "ext";
			}
			extensionLocations.add(extdir);
		}
		String extdir = System.getProperty("argo.ext.dir");
		if (extdir != null) {
			extensionLocations.add(extdir);
		}
	}
	public List<String>getExtensionLocations() {
		return Collections.unmodifiableList(extensionLocations);
	}
	private void huntModulesFromNamedDirectory(String dirname) {
		File extensionDir = new File(dirname);
		if (extensionDir.isDirectory()) {
			File[]files = extensionDir.listFiles(new JarFileFilter());
			for (File file:files) {
				JarFile jarfile = null;
				try {
					jarfile = new JarFile(file);
					if (jarfile != null) {
						ClassLoader classloader = new URLClassLoader(new URL[] {file.toURI().toURL()});
						try {
							processJarFile(classloader,file);
						}catch (ClassNotFoundException e) {
							return;
						}
					}
				}catch (IOException ioe) {
				}
			}
		}
	}
	private void processJarFile(ClassLoader classloader,File file)throws ClassNotFoundException {
		JarFile jarfile;
		try {
			jarfile = new JarFile(file);
		}catch (IOException e) {
			return;
		}
		Manifest manifest;
		try {
			manifest = jarfile.getManifest();
			if (manifest == null) {
			}
		}catch (IOException e) {
			return;
		}
		boolean loadedClass = false;
		if (manifest == null) {
			Enumeration<JarEntry>jarEntries = jarfile.entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry entry = jarEntries.nextElement();
				loadedClass = loadedClass|processEntry(classloader,entry.getName());
			}
		}else {
			Map<String,Attributes>entries = manifest.getEntries();
			for (String key:entries.keySet()) {
				loadedClass = loadedClass|processEntry(classloader,key);
			}
		}
		Translator.addClassLoader(classloader);
	}
	private boolean processEntry(ClassLoader classloader,String cname)throws ClassNotFoundException {
		if (cname.endsWith(CLASS_SUFFIX)) {
			int classNamelen = cname.length() - CLASS_SUFFIX.length();
			String className = cname.substring(0,classNamelen);
			className = className.replace('/','.');
			return addClass(classloader,className);
		}
		return false;
	}
	public static void addClass(String classname)throws ClassNotFoundException {
		getInstance().addClass(ModuleLoader2.class.getClassLoader(),classname);
	}
	private boolean addClass(ClassLoader classLoader,String classname)throws ClassNotFoundException {
		Class moduleClass;
		try {
			moduleClass = classLoader.loadClass(classname);
		}catch (UnsupportedClassVersionError e) {
			return false;
		}catch (NoClassDefFoundError e) {
			return false;
		}catch (Throwable e) {
			if (e instanceof ClassNotFoundException) {
				throw(ClassNotFoundException) e;
			}
			return false;
		}
		if (!ModuleInterface.class.isAssignableFrom(moduleClass)) {
			return false;
		}
		Constructor defaultConstructor;
		try {
			defaultConstructor = moduleClass.getDeclaredConstructor(new Class[] {});
		}catch (SecurityException e) {
			return false;
		}catch (NoSuchMethodException e) {
			return false;
		}catch (NoClassDefFoundError e) {
			return false;
		}catch (Throwable e) {
			return false;
		}
		if (!Modifier.isPublic(defaultConstructor.getModifiers())) {
			return false;
		}
		Object moduleInstance;
		try {
			moduleInstance = defaultConstructor.newInstance(new Object[] {});
		}catch (IllegalArgumentException e) {
			return false;
		}catch (InstantiationException e) {
			return false;
		}catch (IllegalAccessException e) {
			return false;
		}catch (InvocationTargetException e) {
			return false;
		}catch (NoClassDefFoundError e) {
			return false;
		}catch (Throwable e) {
			return false;
		}
		if (!(moduleInstance instanceof ModuleInterface)) {
			return false;
		}
		ModuleInterface mf = (ModuleInterface) moduleInstance;
		addModule(mf);
		return true;
	}
	private void addModule(ModuleInterface mf) {
		for (ModuleInterface foundMf:moduleStatus.keySet()) {
			if (foundMf.getName().equals(mf.getName())) {
				return;
			}
		}
		ModuleStatus ms = new ModuleStatus();
		ms.setSelected();
		moduleStatus.put(mf,ms);
	}
	static class JarFileFilter implements FileFilter {
	public boolean accept(File pathname) {
		return(pathname.canRead()&&pathname.isFile()&&pathname.getPath().toLowerCase().endsWith(".jar"));
	}
}
}

class ModuleStatus {
	private boolean enabled;
	private boolean selected;
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled() {
		enabled = true;
	}
	public void setDisabled() {
		enabled = false;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected() {
		selected = true;
	}
	public void setUnselect() {
		selected = false;
	}
	public void setSelected(boolean value) {
		if (value) {
			setSelected();
		}else {
			setUnselect();
		}
	}
}



