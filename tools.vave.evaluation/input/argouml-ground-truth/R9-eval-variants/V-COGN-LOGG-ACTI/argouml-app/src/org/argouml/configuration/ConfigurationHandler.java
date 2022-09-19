package org.argouml.configuration;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import org.apache.log4j.Logger;


public abstract class ConfigurationHandler {
	private File loadedFromFile;
	private URL loadedFromURL;
	private boolean changeable;
	private boolean loaded;
	private static PropertyChangeSupport pcl;
	private static final Logger LOG = Logger.getLogger(ConfigurationHandler.class);
	public ConfigurationHandler() {
		this(true);
	}
	public ConfigurationHandler(boolean c) {
		super();
		changeable = c;
	}
	public abstract String getDefaultPath();
	private void loadIfNecessary() {
		if (!loaded) {
			loadDefault();
		}
	}
	public final boolean loadDefault() {
		if (loaded) {
			return false;
		}
		boolean status = load(new File(getDefaultPath()));
		if (!status) {
			status = loadUnspecified();
		}
		loaded = true;
		return status;
	}
	public final boolean saveDefault() {
		return saveDefault(false);
	}
	public final boolean saveDefault(boolean force) {
		if (force) {
			File toFile = new File(getDefaultPath());
			boolean saved = saveFile(toFile);
			if (saved) {
				loadedFromFile = toFile;
			}
			return saved;
		}
		if (!loaded) {
			return false;
		}
		if (loadedFromFile != null) {
			return saveFile(loadedFromFile);
		}
		if (loadedFromURL != null) {
			return saveURL(loadedFromURL);
		}
		return false;
	}
	public final boolean isChangeable() {
		return changeable;
	}
	public final boolean isLoaded() {
		return loaded;
	}
	public final boolean load(File file) {
		boolean status = loadFile(file);
		if (status) {
			if (pcl != null) {
				pcl.firePropertyChange(Configuration.FILE_LOADED,null,file);
			}
			loadedFromFile = file;
		}
		return status;
	}
	public final boolean load(URL url) {
		boolean status = loadURL(url);
		if (status) {
			if (pcl != null) {
				pcl.firePropertyChange(Configuration.URL_LOADED,null,url);
			}
			loadedFromURL = url;
		}
		return status;
	}
	public final boolean save(File file) {
		if (!loaded) {
			return false;
		}
		boolean status = saveFile(file);
		if (status) {
			if (pcl != null) {
				pcl.firePropertyChange(Configuration.FILE_SAVED,null,file);
			}
		}
		return status;
	}
	public final boolean save(URL url) {
		if (!loaded) {
			return false;
		}
		boolean status = saveURL(url);
		if (status) {
			if (pcl != null) {
				pcl.firePropertyChange(Configuration.URL_SAVED,null,url);
			}
		}
		return status;
	}
	public final String getString(ConfigurationKey key,String defaultValue) {
		loadIfNecessary();
		return getValue(key.getKey(),defaultValue);
	}
	public final int getInteger(ConfigurationKey key,int defaultValue) {
		loadIfNecessary();
		try {
			String s = getValue(key.getKey(),Integer.toString(defaultValue));
			return Integer.parseInt(s);
		}catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	public final double getDouble(ConfigurationKey key,double defaultValue) {
		loadIfNecessary();
		try {
			String s = getValue(key.getKey(),Double.toString(defaultValue));
			return Double.parseDouble(s);
		}catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}
	public final boolean getBoolean(ConfigurationKey key,boolean defaultValue) {
		loadIfNecessary();
		Boolean dflt = Boolean.valueOf(defaultValue);
		Boolean b = key != null?Boolean.valueOf(getValue(key.getKey(),dflt.toString())):dflt;
		return b.booleanValue();
	}
	private synchronized void workerSetValue(ConfigurationKey key,String newValue) {
		loadIfNecessary();
		String oldValue = getValue(key.getKey(),"");
		setValue(key.getKey(),newValue);
		if (pcl != null) {
			pcl.firePropertyChange(key.getKey(),oldValue,newValue);
		}
	}
	public final void setString(ConfigurationKey key,String newValue) {
		workerSetValue(key,newValue);
	}
	public final void setInteger(ConfigurationKey key,int value) {
		workerSetValue(key,Integer.toString(value));
	}
	public final void setDouble(ConfigurationKey key,double value) {
		workerSetValue(key,Double.toString(value));
	}
	public final void setBoolean(ConfigurationKey key,boolean value) {
		Boolean bool = Boolean.valueOf(value);
		workerSetValue(key,bool.toString());
	}
	public final void addListener(PropertyChangeListener p) {
		if (pcl == null) {
			pcl = new PropertyChangeSupport(this);
		}
		LOG.debug("addPropertyChangeListener(" + p + ")");
		pcl.addPropertyChangeListener(p);
	}
	public final void removeListener(PropertyChangeListener p) {
		if (pcl != null) {
			LOG.debug("removePropertyChangeListener()");
			pcl.removePropertyChangeListener(p);
		}
	}
	public final void addListener(ConfigurationKey key,PropertyChangeListener p) {
		if (pcl == null) {
			pcl = new PropertyChangeSupport(this);
		}
		LOG.debug("addPropertyChangeListener(" + key.getKey() + ")");
		pcl.addPropertyChangeListener(key.getKey(),p);
	}
	public final void removeListener(ConfigurationKey key,PropertyChangeListener p) {
		if (pcl != null) {
			LOG.debug("removePropertyChangeListener(" + key.getKey() + ")");
			pcl.removePropertyChangeListener(key.getKey(),p);
		}
	}
	boolean loadUnspecified() {
		return false;
	}
	boolean saveUnspecified() {
		return false;
	}
	public abstract boolean loadFile(File file);
	public abstract boolean loadURL(URL url);
	public abstract boolean saveFile(File file);
	public abstract boolean saveURL(URL url);
	public boolean hasKey(ConfigurationKey key) {
		return getValue(key.getKey(),"true").equals(getValue(key.getKey(),"false"));
	}
	public abstract String getValue(String key,String defaultValue);
	public abstract void setValue(String key,String value);
	public abstract void remove(String key);
}



