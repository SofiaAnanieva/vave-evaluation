package org.argouml.model;

import java.beans.PropertyChangeListener;


public abstract class AbstractModelEventPump implements ModelEventPump {
	public abstract void addModelEventListener(PropertyChangeListener listener,Object modelelement,String[]eventNames);
	public void addModelEventListener(PropertyChangeListener listener,Object modelelement,String eventName) {
		addModelEventListener(listener,modelelement,new String[] {eventName});
	}
	public abstract void addModelEventListener(PropertyChangeListener listener,Object modelelement);
	public abstract void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String[]eventNames);
	public void removeModelEventListener(PropertyChangeListener listener,Object modelelement,String eventName) {
		removeModelEventListener(listener,modelelement,new String[] {eventName});
	}
	public abstract void removeModelEventListener(PropertyChangeListener listener,Object modelelement);
	public abstract void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]eventNames);
	public void addClassModelEventListener(PropertyChangeListener listener,Object modelClass,String eventName) {
		addClassModelEventListener(listener,modelClass,new String[] {eventName});
	}
	public abstract void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String[]eventNames);
	public void removeClassModelEventListener(PropertyChangeListener listener,Object modelClass,String eventName) {
		removeClassModelEventListener(listener,modelClass,new String[] {eventName});
	}
}



