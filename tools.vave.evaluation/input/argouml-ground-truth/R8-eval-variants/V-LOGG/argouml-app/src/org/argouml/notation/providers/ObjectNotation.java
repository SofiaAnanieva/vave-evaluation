package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class ObjectNotation extends NotationProvider {
	public ObjectNotation(Object theObject) {
		if (!Model.getFacade().isAObject(theObject)) {
			throw new IllegalArgumentException("This is not an Object.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"name","classifier"});
		Collection c = Model.getFacade().getClassifiers(modelElement);
		Iterator i = c.iterator();
		while (i.hasNext()) {
			Object st = i.next();
			addElementListener(listener,st,"name");
		}
	}
	public void updateListener(PropertyChangeListener listener,Object modelElement,PropertyChangeEvent pce) {
		if (pce instanceof AttributeChangeEvent&&pce.getSource() == modelElement&&"classifier".equals(pce.getPropertyName())) {
			if (pce.getOldValue() != null) {
				removeElementListener(listener,pce.getOldValue());
			}
			if (pce.getNewValue() != null) {
				addElementListener(listener,pce.getNewValue(),"name");
			}
		}
	}
}



