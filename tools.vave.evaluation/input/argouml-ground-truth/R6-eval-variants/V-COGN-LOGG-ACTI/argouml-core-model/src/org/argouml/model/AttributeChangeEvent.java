package org.argouml.model;

import java.util.EventObject;


public class AttributeChangeEvent extends UmlChangeEvent {
	public AttributeChangeEvent(Object source,String propertyName,Object oldValue,Object newValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,originalEvent);
	}
	private static final long serialVersionUID = 1573202490278617016l;
}



