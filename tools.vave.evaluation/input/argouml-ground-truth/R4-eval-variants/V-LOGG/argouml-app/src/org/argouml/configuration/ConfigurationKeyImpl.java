package org.argouml.configuration;

import java.beans.PropertyChangeEvent;


public class ConfigurationKeyImpl implements ConfigurationKey {
	private String key = null;
	public ConfigurationKeyImpl(String k1) {
		key = "argo." + k1;
	}
	public ConfigurationKeyImpl(ConfigurationKey ck,String k1) {
		key = ck.getKey() + "." + k1;
	}
	public ConfigurationKeyImpl(String k1,String k2) {
		key = "argo." + k1 + "." + k2;
	}
	public ConfigurationKeyImpl(String k1,String k2,String k3) {
		key = "argo." + k1 + "." + k2 + "." + k3;
	}
	public ConfigurationKeyImpl(String k1,String k2,String k3,String k4) {
		key = "argo." + k1 + "." + k2 + "." + k3 + "." + k4;
	}
	public ConfigurationKeyImpl(String k1,String k2,String k3,String k4,String k5) {
		key = "argo." + k1 + "." + k2 + "." + k3 + "." + k4 + "." + k5;
	}
	public final String getKey() {
		return key;
	}
	public boolean isChangedProperty(PropertyChangeEvent pce) {
		if (pce == null) {
			return false;
		}
		return pce.getPropertyName().equals(key);
	}
	public String toString() {
		return"{ConfigurationKeyImpl:" + key + "}";
	}
}



