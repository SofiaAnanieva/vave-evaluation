package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class AssociationNameNotation extends NotationProvider {
	public AssociationNameNotation(Object modelElement) {
		if (!Model.getFacade().isAAssociation(modelElement)) {
			throw new IllegalArgumentException("This is not an Association.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"name","visibility","stereotype"});
		Collection stereotypes = Model.getFacade().getStereotypes(modelElement);
		Iterator iter = stereotypes.iterator();
		while (iter.hasNext()) {
			Object oneStereoType = iter.next();
			addElementListener(listener,oneStereoType,new String[] {"name","remove"});
		}
	}
}



