package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;


public abstract class AssociationEndNameNotation extends NotationProvider {
	protected AssociationEndNameNotation() {
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"name","visibility","stereotype"});
		Collection stereotypes = Model.getFacade().getStereotypes(modelElement);
		Iterator iter = stereotypes.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			addElementListener(listener,o,new String[] {"name","remove"});
		}
	}
	public void updateListener(PropertyChangeListener listener,Object modelElement,PropertyChangeEvent pce) {
		Object obj = pce.getSource();
		if ((obj == modelElement)&&"stereotype".equals(pce.getPropertyName())) {
			if (pce instanceof AddAssociationEvent&&Model.getFacade().isAStereotype(pce.getNewValue())) {
				addElementListener(listener,pce.getNewValue(),new String[] {"name","remove"});
			}
			if (pce instanceof RemoveAssociationEvent&&Model.getFacade().isAStereotype(pce.getOldValue())) {
				removeElementListener(listener,pce.getOldValue());
			}
		}
	}
}



