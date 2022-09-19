package org.argouml.model;

import java.util.EventObject;


public class AssociationChangeEvent extends UmlChangeEvent {
	private Object changedValue;
	public AssociationChangeEvent(Object source,String propertyName,Object oldValue,Object newValue,Object theChangedValue,EventObject originalEvent) {
		super(source,propertyName,oldValue,newValue,originalEvent);
		changedValue = theChangedValue;
	}
	public Object getChangedValue() {
		return changedValue;
	}
	private static final long serialVersionUID = 6586460366990334839l;
}



