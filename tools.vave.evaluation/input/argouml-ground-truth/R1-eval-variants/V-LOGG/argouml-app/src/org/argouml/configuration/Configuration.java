package org.argouml.configuration;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;


public final class Configuration {
	public static final String FILE_LOADED = "configuration.load.file";
	public static final String URL_LOADED = "configuration.load.url";
	public static final String FILE_SAVED = "configuration.save.file";
	public static final String URL_SAVED = "configuration.save.url";
	private static ConfigurationHandler config = getFactory().getConfigurationHandler();
	private Configuration() {
	}
	public static ConfigurationHandler getConfigurationHandler() {
		return config;
	}
	public static IConfigurationFactory getFactory() {
		return ConfigurationFactory.getInstance();
	}
	public static boolean load() {
		return config.loadDefault();
	}
	public static boolean load(File file) {
		return config.load(file);
	}
	public static boolean load(URL url) {
		return config.load(url);
	}
	public static boolean save() {
		return Configuration.save(false);
	}
	public static boolean save(boolean force) {
		return config.saveDefault(force);
	}
	public static String getString(ConfigurationKey key) {
		return getString(key,"");
	}
	public static String getString(ConfigurationKey key,String defaultValue) {
		return config.getString(key,defaultValue);
	}
	public static int getInteger(ConfigurationKey key) {
		return getInteger(key,0);
	}
	public static double getDouble(ConfigurationKey key,double defaultValue) {
		return config.getDouble(key,defaultValue);
	}
	public static double getDouble(ConfigurationKey key) {
		return getDouble(key,0);
	}
	public static int getInteger(ConfigurationKey key,int defaultValue) {
		return config.getInteger(key,defaultValue);
	}
	public static boolean getBoolean(ConfigurationKey key) {
		return getBoolean(key,false);
	}
	public static boolean getBoolean(ConfigurationKey key,boolean defaultValue) {
		return config.getBoolean(key,defaultValue);
	}
	public static void setString(ConfigurationKey key,String newValue) {
		config.setString(key,newValue);
	}
	public static void setInteger(ConfigurationKey key,int newValue) {
		config.setInteger(key,newValue);
	}
	public static void setDouble(ConfigurationKey key,double newValue) {
		config.setDouble(key,newValue);
	}
	public static void setBoolean(ConfigurationKey key,boolean newValue) {
		config.setBoolean(key,newValue);
	}
	public static void addListener(PropertyChangeListener pcl) {
		config.addListener(pcl);
	}
	public static void removeListener(PropertyChangeListener pcl) {
		config.removeListener(pcl);
	}
	public static void addListener(ConfigurationKey key,PropertyChangeListener pcl) {
		config.addListener(key,pcl);
	}
	public static void removeListener(ConfigurationKey key,PropertyChangeListener pcl) {
		config.removeListener(key,pcl);
	}
	public static void removeKey(ConfigurationKey key) {
		config.remove(key.getKey());
	}
	public static ConfigurationKey makeKey(String k1) {
		return new ConfigurationKeyImpl(k1);
	}
	public static ConfigurationKey makeKey(ConfigurationKey ck,String k1) {
		return new ConfigurationKeyImpl(ck,k1);
	}
	public static ConfigurationKey makeKey(String k1,String k2) {
		return new ConfigurationKeyImpl(k1,k2);
	}
	public static ConfigurationKey makeKey(String k1,String k2,String k3) {
		return new ConfigurationKeyImpl(k1,k2,k3);
	}
	public static ConfigurationKey makeKey(String k1,String k2,String k3,String k4) {
		return new ConfigurationKeyImpl(k1,k2,k3,k4);
	}
	public static ConfigurationKey makeKey(String k1,String k2,String k3,String k4,String k5) {
		return new ConfigurationKeyImpl(k1,k2,k3,k4,k5);
	}
}



