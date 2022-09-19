package org.argouml.configuration;


public class ConfigurationFactory implements IConfigurationFactory {
	private static final IConfigurationFactory SINGLETON;
	private static ConfigurationHandler handler = new ConfigurationProperties();
	static {
	String name = System.getProperty("argo.ConfigurationFactory");
	IConfigurationFactory newFactory = null;
	if (name != null) {
		try {
			newFactory = (IConfigurationFactory) Class.forName(name).newInstance();
		}catch (Exception e) {
		}
	}
	if (newFactory == null) {
		newFactory = new ConfigurationFactory();
	}
	SINGLETON = newFactory;
}
	private ConfigurationFactory() {
	}
	public static final IConfigurationFactory getInstance() {
		return SINGLETON;
	}
	public ConfigurationHandler getConfigurationHandler() {
		return handler;
	}
}



