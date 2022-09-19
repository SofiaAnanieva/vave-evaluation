package org.argouml.model;

import java.beans.PropertyChangeListener;
import java.util.List;


public interface ModelEventPump {
	void addModelEventListener(PropertyChangeListener listener,Object modelelement,String[]propertyNames);
	void addModelEventListener(PropertyChangeListener listener,Object modelelement,String propertyName);
	void addModelEventListener(PropertyChangeListener listener,Object modelelement);
	void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String[]propertyNames);
	void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String propertyName);
	void removeModelEventListener(PropertyChangeListener listener,Object modelelement);
	void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames);
	void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String propertyName);
	void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]propertyNames);
	void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String propertyName);
	void startPumpingEvents();
	void stopPumpingEvents();
	void flushModelEvents();
	@SuppressWarnings("unchecked")public List getDebugInfo();
}



