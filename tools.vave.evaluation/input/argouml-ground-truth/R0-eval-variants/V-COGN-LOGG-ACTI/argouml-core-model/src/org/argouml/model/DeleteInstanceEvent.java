package org.argouml.model;

import java.util.EventObject;


public class DeleteInstanceEvent extends UmlChangeEvent {
	public DeleteInstanceEvent(Object source,String propertyName,Object oldValue,Object newValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,originalEvent);
	}
	private static final long serialVersionUID = 650590690953566827l;
}



