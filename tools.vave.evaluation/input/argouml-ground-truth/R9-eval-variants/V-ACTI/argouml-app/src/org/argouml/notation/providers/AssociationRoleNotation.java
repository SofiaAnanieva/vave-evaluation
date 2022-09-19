package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class AssociationRoleNotation extends NotationProvider {
	public AssociationRoleNotation(Object role) {
		if (!Model.getFacade().isAAssociationRole(role)) {
			throw new IllegalArgumentException("This is not an AssociationRole.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"name","base"});
		Object assoc = Model.getFacade().getBase(modelElement);
		if (assoc != null) {
			addElementListener(listener,assoc,"name");
		}
	}
}



