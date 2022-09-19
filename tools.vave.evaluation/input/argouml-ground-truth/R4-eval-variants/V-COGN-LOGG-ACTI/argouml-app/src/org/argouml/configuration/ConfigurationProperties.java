package org.argouml.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;


class ConfigurationProperties extends ConfigurationHandler {
	private static final Logger LOG = Logger.getLogger(ConfigurationProperties.class);
	private static String propertyLocation = "/org/argouml/resource/default.properties";
	private Properties propertyBundle;
	private boolean canComplain = true;
	ConfigurationProperties() {
			super(true);
			Properties defaults = new Properties();
			try {
				defaults.load(getClass().getResourceAsStream(propertyLocation));
				LOG.debug("Configuration loaded from " + propertyLocation);
			}catch (Exception ioe) {
				LOG.warn("Configuration not loaded from " + propertyLocation,ioe);
			}
			propertyBundle = new Properties(defaults);
		}
	public String getDefaultPath() {
		return System.getProperty("user.home") + "/.argouml/argo.user.properties";
	}
	@Deprecated public String getOldDefaultPath() {
		return System.getProperty("user.home") + "/argo.user.properties";
	}
	private static boolean copyFile(final File source,final File dest) {
		try {
			final FileInputStream fis = new FileInputStream(source);
			final FileOutputStream fos = new FileOutputStream(dest);
			byte[]buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf,0,i);
			}
			fis.close();
			fos.close();
			return true;
		}catch (final FileNotFoundException e) {
			LOG.error("File not found while copying",e);
			return false;
		}catch (final IOException e) {
			LOG.error("IO error copying file",e);
			return false;
		}catch (final SecurityException e) {
			LOG.error("You are not allowed to copy these files",e);
			return false;
		}
	}
	public boolean loadFile(File file) {
		try {
			if (!file.exists()) {
				final File oldFile = new File(getOldDefaultPath());
				if (oldFile.exists()&&oldFile.isFile()&&oldFile.canRead()&&file.getParentFile().canWrite()) {
					final boolean result = copyFile(oldFile,file);
					if (result) {
						LOG.info("Configuration copied from " + oldFile + " to " + file);
					}else {
						LOG.error("Error copying old configuration to new, " + "see previous log messages");
					}
				}else {
					try {
						file.createNewFile();
					}catch (IOException e) {
						LOG.error("Could not create the properties file at: " + file.getAbsolutePath(),e);
					}
				}
			}
			if (file.exists()&&file.isFile()&&file.canRead()) {
				try {
					propertyBundle.load(new FileInputStream(file));
					LOG.info("Configuration loaded from " + file);
					return true;
				}catch (final IOException e) {
					if (canComplain) {
						LOG.warn("Unable to load configuration " + file);
					}
					canComplain = false;
				}
			}
		}catch (final SecurityException e) {
			LOG.error("A security exception occurred trying to load" + " the configuration, check your security settings",e);
		}
		return false;
	}
	public boolean saveFile(File file) {
		try {
			propertyBundle.store(new FileOutputStream(file),"ArgoUML properties");
			LOG.info("Configuration saved to " + file);
			return true;
		}catch (Exception e) {
			if (canComplain) {
				LOG.warn("Unable to save configuration " + file + "\n");
			}
			canComplain = false;
		}
		return false;
	}
	public boolean loadURL(URL url) {
		try {
			propertyBundle.load(url.openStream());
			LOG.info("Configuration loaded from " + url + "\n");
			return true;
		}catch (Exception e) {
			if (canComplain) {
				LOG.warn("Unable to load configuration " + url + "\n");
			}
			canComplain = false;
			return false;
		}
	}
	public boolean saveURL(URL url) {
		return false;
	}
	public String getValue(String key,String defaultValue) {
		String result = "";
		try {
			result = propertyBundle.getProperty(key,defaultValue);
		}catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}
	public void setValue(String key,String value) {
		LOG.debug("key \'" + key + "\' set to \'" + value + "\'");
		propertyBundle.setProperty(key,value);
	}
	public void remove(String key) {
		propertyBundle.remove(key);
	}
}



