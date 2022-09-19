package org.argouml.application.api;

import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;


public final class Argo {
	@Deprecated public static final String ARGOINI = "/org/argouml/argo.ini";
	public static final ConfigurationKey KEY_STARTUP_DIR = Configuration.makeKey("default","user","dir");
	public static final ConfigurationKey KEY_SPLASH = Configuration.makeKey("init","splash");
	public static final ConfigurationKey KEY_EDEM = Configuration.makeKey("init","edem");
	public static final ConfigurationKey KEY_MOST_RECENT_PROJECT_FILE = Configuration.makeKey("project","mostrecent","file");
	public static final ConfigurationKey KEY_MOST_RECENT_EXPORT_DIRECTORY = Configuration.makeKey("project","mostrecent","exportdirectory");
	public static final ConfigurationKey KEY_RELOAD_RECENT_PROJECT = Configuration.makeKey("init","project","loadmostrecent");
	public static final ConfigurationKey KEY_NUMBER_LAST_RECENT_USED = Configuration.makeKey("project","mostrecent","maxNumber");
	public static final ConfigurationKey KEY_SCREEN_TOP_Y = Configuration.makeKey("screen","top");
	public static final ConfigurationKey KEY_SCREEN_LEFT_X = Configuration.makeKey("screen","left");
	public static final ConfigurationKey KEY_SCREEN_WIDTH = Configuration.makeKey("screen","width");
	public static final ConfigurationKey KEY_SCREEN_HEIGHT = Configuration.makeKey("screen","height");
	public static final ConfigurationKey KEY_SCREEN_MAXIMIZED = Configuration.makeKey("screen","maximized");
	public static final ConfigurationKey KEY_SCREEN_SOUTHWEST_WIDTH = Configuration.makeKey("screen","southwest","width");
	public static final ConfigurationKey KEY_SCREEN_NORTHWEST_WIDTH = Configuration.makeKey("screen","northwest","width");
	public static final ConfigurationKey KEY_SCREEN_SOUTHEAST_WIDTH = Configuration.makeKey("screen","southeast","width");
	public static final ConfigurationKey KEY_SCREEN_NORTHEAST_WIDTH = Configuration.makeKey("screen","northeast","width");
	public static final ConfigurationKey KEY_SCREEN_WEST_WIDTH = Configuration.makeKey("screen","west","width");
	public static final ConfigurationKey KEY_SCREEN_EAST_WIDTH = Configuration.makeKey("screen","east","width");
	public static final ConfigurationKey KEY_SCREEN_SOUTH_HEIGHT = Configuration.makeKey("screen","south","height");
	public static final ConfigurationKey KEY_SCREEN_NORTH_HEIGHT = Configuration.makeKey("screen","north","height");
	public static final ConfigurationKey KEY_SCREEN_THEME = Configuration.makeKey("screen","theme");
	public static final ConfigurationKey KEY_LOOK_AND_FEEL_CLASS = Configuration.makeKey("screen","lookAndFeelClass");
	public static final ConfigurationKey KEY_THEME_CLASS = Configuration.makeKey("screen","themeClass");
	public static final ConfigurationKey KEY_SMOOTH_EDGES = Configuration.makeKey("screen","diagram-antialiasing");
	public static final ConfigurationKey KEY_USER_EMAIL = Configuration.makeKey("user","email");
	public static final ConfigurationKey KEY_USER_FULLNAME = Configuration.makeKey("user","fullname");
	public static final ConfigurationKey KEY_USER_IMPORT_CLASSPATH = Configuration.makeKey("import","clazzpath");
	public static final ConfigurationKey KEY_IMPORT_GENERAL_SETTINGS_FLAGS = Configuration.makeKey("import","general","flags");
	public static final ConfigurationKey KEY_IMPORT_GENERAL_DETAIL_LEVEL = Configuration.makeKey("import","general","detail","level");
	public static final ConfigurationKey KEY_INPUT_SOURCE_ENCODING = Configuration.makeKey("import","file","encoding");
	public static final ConfigurationKey KEY_XMI_STRIP_DIAGRAMS = Configuration.makeKey("import","xmi","stripDiagrams");
	public static final ConfigurationKey KEY_DEFAULT_MODEL = Configuration.makeKey("defaultModel");
	public static final ConfigurationKey KEY_USER_EXPLORER_PERSPECTIVES = Configuration.makeKey("explorer","perspectives");
	public static final ConfigurationKey KEY_LOCALE = Configuration.makeKey("locale");
	public static final ConfigurationKey KEY_GRID = Configuration.makeKey("grid");
	public static final ConfigurationKey KEY_SNAP = Configuration.makeKey("snap");
	public static final String ARGO_CONSOLE_SUPPRESS = "argo.console.suppress";
	public static final String ARGO_CONSOLE_PREFIX = "argo.console.prefix";
	public static final String DOCUMENTATION_TAG = "documentation";
	public static final String AUTHOR_TAG = "author";
	public static final String SINCE_TAG = "since";
	public static final String SEE_TAG = "see";
	public static final String DEPRECATED_TAG = "deprecated";
	public static final String VERSION_TAG = "version";
	@Deprecated public static final String DOCUMENTATION_TAG_ALT = "javadocs";
	public static final int SCOPE_APPLICATION = 0;
	public static final int SCOPE_PROJECT = 1;
	private Argo() {
	}
	public static void setDirectory(String dir) {
		org.tigris.gef.base.Globals.setLastDirectory(dir);
	}
	public static String getDirectory() {
		return Configuration.getString(KEY_STARTUP_DIR,org.tigris.gef.base.Globals.getLastDirectory());
	}
	public static String getEncoding() {
		return"UTF-8";
	}
}



