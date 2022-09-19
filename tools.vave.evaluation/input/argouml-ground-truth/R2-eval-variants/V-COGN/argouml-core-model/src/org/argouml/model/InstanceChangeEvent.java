package org.argouml.model;

import java.util.EventObject;


public abstract class InstanceChangeEvent extends UmlChangeEvent {
	public InstanceChangeEvent(Object source,String propertyName,Object oldValue,Object newValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,originalEvent);
	}
}



