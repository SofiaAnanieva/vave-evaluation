package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;


public abstract class OperationNotation extends NotationProvider {
	public OperationNotation(Object operation) {
		if (!Model.getFacade().isAOperation(operation)&&!Model.getFacade().isAReception(operation)) {
			throw new IllegalArgumentException("This is not an Operation or Reception.");
		}
	}
	@Override public void initialiseListener(PropertyChangeListener listener,Object modelElement) {
		addElementListener(listener,modelElement);
		if (Model.getFacade().isAOperation(modelElement)) {
			for (Object uml:Model.getFacade().getStereotypes(modelElement)) {
				addElementListener(listener,uml);
			}
			for (Object uml:Model.getFacade().getParameters(modelElement)) {
				addElementListener(listener,uml);
				Object type = Model.getFacade().getType(uml);
				if (type != null) {
					addElementListener(listener,type);
				}
			}
			for (Object uml:Model.getFacade().getTaggedValuesCollection(modelElement)) {
				addElementListener(listener,uml);
			}
		}
	}
	@Override public void updateListener(PropertyChangeListener listener,Object modelElement,PropertyChangeEvent pce) {
		if (pce.getSource() == modelElement&&("stereotype".equals(pce.getPropertyName())||"parameter".equals(pce.getPropertyName())||"taggedValue".equals(pce.getPropertyName()))) {
			if (pce instanceof AddAssociationEvent) {
				addElementListener(listener,pce.getNewValue());
			}
			if (pce instanceof RemoveAssociationEvent) {
				removeElementListener(listener,pce.getOldValue());
			}
		}
		if (!Model.getUmlFactory().isRemoved(modelElement)) {
			for (Object param:Model.getFacade().getParameters(modelElement)) {
				if (pce.getSource() == param&&("type".equals(pce.getPropertyName()))) {
					if (pce instanceof AddAssociationEvent) {
						addElementListener(listener,pce.getNewValue());
					}
					if (pce instanceof RemoveAssociationEvent) {
						removeElementListener(listener,pce.getOldValue());
					}
				}
			}
		}
	}
}



