package org.argouml.notation.providers;

import java.beans.PropertyChangeListener;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;


public abstract class ModelElementNameNotation extends NotationProvider {
	public ModelElementNameNotation(Object modelElement) {
		if (!Model.getFacade().isAModelElement(modelElement)) {
			throw new IllegalArgumentException("This is not a ModelElement.");
		}
	}
	public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement,new String[] {"name","visibility"});
		Object ns = Model.getFacade().getNamespace(modelElement);
		while (ns != null&&!Model.getFacade().isAModel(ns)) {
			addElementListener(listener,ns,new String[] {"name","namespace"});
			ns = Model.getFacade().getNamespace(ns);
		}
	}
}



