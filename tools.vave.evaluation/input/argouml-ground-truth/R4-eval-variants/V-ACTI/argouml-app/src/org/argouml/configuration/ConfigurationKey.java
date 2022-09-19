package org.argouml.configuration;

import java.beans.PropertyChangeEvent;


public interface ConfigurationKey {
	String getKey();
	boolean isChangedProperty(PropertyChangeEvent pce);
}



