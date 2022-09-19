package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;


public abstract class AttributeNotation extends NotationProvider {
	protected AttributeNotation() {
	}
	@Override public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement);
		if (Model.getFacade().isAAttribute(modelElement)) {
			for (Object uml:Model.getFacade().getStereotypes(modelElement)) {
				addElementListener(listener,uml);
			}
			Object type = Model.getFacade().getType(modelElement);
			if (type != null) {
				addElementListener(listener,type);
			}
		}
	}
	@Override public void updateListener(PropertyChangeListener listener,Object modelElement,PropertyChangeEvent pce) {
		if (pce.getSource() == modelElement&&("stereotype".equals(pce.getPropertyName())||("type".equals(pce.getPropertyName())))) {
			if (pce instanceof AddAssociationEvent) {
				addElementListener(listener,pce.getNewValue());
			}
			if (pce instanceof RemoveAssociationEvent) {
				removeElementListener(listener,pce.getOldValue());
			}
		}
	}
}



