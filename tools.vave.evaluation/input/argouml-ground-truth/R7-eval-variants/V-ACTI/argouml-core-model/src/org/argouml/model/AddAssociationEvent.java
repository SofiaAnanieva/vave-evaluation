package org.argouml.model;

import java.util.EventObject;


public class AddAssociationEvent extends AssociationChangeEvent {
	public AddAssociationEvent(Object source,String propertyName,Object oldValue,Object newValue,Object changedValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,changedValue,originalEvent);
	}
	private static final long serialVersionUID = 1672552190650651905l;
}



