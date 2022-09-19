package org.argouml.model;

import java.beans.PropertyChangeEvent;
import java.util.EventObject;


public abstract class UmlChangeEvent extends PropertyChangeEvent {
	private EventObject originalEvent;
	public UmlChangeEvent(Object source,String propertyName,Object oldValue,Object newValue,EventObject theOriginalEvent) {
		super(source,propertyName,oldValue,newValue);
		originalEvent = theOriginalEvent;
	}
	protected EventObject getOriginalEvent() {
		return originalEvent;
	}
	@Override public String toString() {
		return super.toString() + ": " + originalEvent;
	}
}



