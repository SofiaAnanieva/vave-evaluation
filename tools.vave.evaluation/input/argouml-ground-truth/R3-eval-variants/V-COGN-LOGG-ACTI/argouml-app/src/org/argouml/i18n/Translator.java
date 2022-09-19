package org.argouml.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.tigris.gef.util.Localizer;


public final class Translator {
	private static final Logger LOG = Logger.getLogger(Translator.class);
	private static final String BUNDLES_PATH = "org.argouml.i18n";
	private static Map<String,ResourceBundle>bundles;
	private static List<ClassLoader>classLoaders = new ArrayList<ClassLoader>();
	private static boolean initialized;
	private static Locale systemDefaultLocale;
	private Translator() {
	}
	public static void initForEclipse(String locale) {
		initInternal(locale);
	}
	public static void init(String locale) {
		initialized = true;
		systemDefaultLocale = Locale.getDefault();
		if ((!"".equals(locale))&&(locale != null)) {
			setLocale(locale);
		}else {
			setLocale(new Locale(System.getProperty("user.language","en"),System.getProperty("user.country","")));
		}
		Localizer.addResource("GefBase","org.tigris.gef.base.BaseResourceBundle");
		Localizer.addResource("GefPres","org.tigris.gef.presentation.PresentationResourceBundle");
	}
	private static void initInternal(String s) {
		assert!initialized;
		initialized = true;
		systemDefaultLocale = Locale.getDefault();
		if ((!"".equals(s))&&(s != null)) {
			setLocale(s);
		}else {
			setLocale(new Locale(System.getProperty("user.language","en"),System.getProperty("user.country","")));
		}
		Localizer.addResource("GefBase","org.tigris.gef.base.BaseResourceBundle");
		Localizer.addResource("GefPres","org.tigris.gef.presentation.PresentationResourceBundle");
	}
	public static Locale[]getLocales() {
		return new Locale[] {Locale.ENGLISH,Locale.FRENCH,new Locale("es",""),Locale.GERMAN,Locale.ITALIAN,new Locale("nb",""),new Locale("pt",""),new Locale("ru",""),Locale.CHINESE,Locale.UK};
	}
	public static void setLocale(String name) {
		if (!initialized) {
			init("en");
		}
		String language = name;
		String country = "";
		int i = name.indexOf("_");
		if ((i > 0)&&(name.length() > i + 1)) {
			language = name.substring(0,i);
			country = name.substring(i + 1);
		}
		setLocale(new Locale(language,country));
	}
	public static void setLocale(Locale locale) {
		Locale.setDefault(locale);
		bundles = new HashMap<String,ResourceBundle>();
	}
	public static Locale getSystemDefaultLocale() {
		return systemDefaultLocale;
	}
	public static void addClassLoader(ClassLoader cl) {
		classLoaders.add(cl);
	}
	private static void loadBundle(String name) {
		if (bundles.containsKey(name)) {
			return;
		}
		String resource = BUNDLES_PATH + "." + name;
		ResourceBundle bundle = null;
		try {
			LOG.debug("Loading " + resource);
			Locale locale = Locale.getDefault();
			bundle = ResourceBundle.getBundle(resource,locale);
		}catch (MissingResourceException e1) {
			LOG.debug("Resource " + resource + " not found in the default class loader.");
			Iterator iter = classLoaders.iterator();
			while (iter.hasNext()) {
				ClassLoader cl = (ClassLoader) iter.next();
				try {
					LOG.debug("Loading " + resource + " from " + cl);
					bundle = ResourceBundle.getBundle(resource,Locale.getDefault(),cl);
					break;
				}catch (MissingResourceException e2) {
					LOG.debug("Resource " + resource + " not found in " + cl);
				}
			}
		}
		bundles.put(name,bundle);
	}
	private static String getName(String key) {
		if (key == null) {
			return null;
		}
		int indexOfDot = key.indexOf(".");
		if (indexOfDot > 0) {
			return key.substring(0,indexOfDot);
		}
		return null;
	}
	public static String localize(String key,Object[]args) {
		return messageFormat(key,args);
	}
	public static String localize(String key) {
		if (!initialized) {
			init("en");
		}
		if (key == null) {
			throw new IllegalArgumentException("null");
		}
		String name = getName(key);
		if (name == null) {
			return Localizer.localize("UMLMenu",key);
		}
		loadBundle(name);
		ResourceBundle bundle = bundles.get(name);
		if (bundle == null) {
			LOG.debug("Bundle (" + name + ") for resource " + key + " not found.");
			return key;
		}
		try {
			return bundle.getString(key);
		}catch (MissingResourceException e) {
			LOG.debug("Resource " + key + " not found.");
			return key;
		}
	}
	public static String messageFormat(String key,Object[]args) {
		return new MessageFormat(localize(key)).format(args);
	}
}



