package org.argouml.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


class ConfigurationProperties extends ConfigurationHandler {
	private static String propertyLocation = "/org/argouml/resource/default.properties";
	private Properties propertyBundle;
	private boolean canComplain = true;
	ConfigurationProperties() {
			super(true);
			Properties defaults = new Properties();
			try {
				defaults.load(getClass().getResourceAsStream(propertyLocation));
			}catch (Exception ioe) {
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
			return false;
		}catch (final IOException e) {
			return false;
		}catch (final SecurityException e) {
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
					}else {
					}
				}else {
					try {
						file.createNewFile();
					}catch (IOException e) {
					}
				}
			}
			if (file.exists()&&file.isFile()&&file.canRead()) {
				try {
					propertyBundle.load(new FileInputStream(file));
					return true;
				}catch (final IOException e) {
					canComplain = false;
				}
			}
		}catch (final SecurityException e) {
		}
		return false;
	}
	public boolean saveFile(File file) {
		try {
			propertyBundle.store(new FileOutputStream(file),"ArgoUML properties");
			return true;
		}catch (Exception e) {
			canComplain = false;
		}
		return false;
	}
	public boolean loadURL(URL url) {
		try {
			propertyBundle.load(url.openStream());
			return true;
		}catch (Exception e) {
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
		propertyBundle.setProperty(key,value);
	}
	public void remove(String key) {
		propertyBundle.remove(key);
	}
}



