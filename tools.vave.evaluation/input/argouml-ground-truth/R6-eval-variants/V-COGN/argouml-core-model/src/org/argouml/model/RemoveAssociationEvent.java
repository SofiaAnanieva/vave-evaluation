package org.argouml.model;

import java.util.EventObject;


public class RemoveAssociationEvent extends AssociationChangeEvent {
	public RemoveAssociationEvent(Object source,String propertyName,Object oldValue,Object newValue,Object changedValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,changedValue,originalEvent);
	}
	private static final long serialVersionUID = -1657123224250248465l;
}



