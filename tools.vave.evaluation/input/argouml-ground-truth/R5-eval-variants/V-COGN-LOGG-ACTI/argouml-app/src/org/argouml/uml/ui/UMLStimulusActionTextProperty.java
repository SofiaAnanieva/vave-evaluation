package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLStimulusActionTextProperty {
	private String thePropertyName;
	public UMLStimulusActionTextProperty(String propertyName) {
		thePropertyName = propertyName;
	}
	public void setProperty(UMLUserInterfaceContainer container,String newValue) {
		Object stimulus = container.getTarget();
		if (stimulus != null) {
			String oldValue = getProperty(container);
			if (newValue == null||oldValue == null||!newValue.equals(oldValue)) {
				if (newValue != oldValue) {
					Object action = Model.getFacade().getDispatchAction(stimulus);
					Model.getCoreHelper().setName(action,newValue);
					String dummyStr = Model.getFacade().getName(stimulus);
					Model.getCoreHelper().setName(stimulus,dummyStr);
				}
			}
		}
	}
	public String getProperty(UMLUserInterfaceContainer container) {
		String value = null;
		Object stimulus = container.getTarget();
		if (stimulus != null) {
			Object action = Model.getFacade().getDispatchAction(stimulus);
			if (action != null) {
				value = Model.getFacade().getName(action);
			}
		}
		return value;
	}
	boolean isAffected(PropertyChangeEvent event) {
		String sourceName = event.getPropertyName();
		return(thePropertyName == null||sourceName == null||sourceName.equals(thePropertyName));
	}
	void targetChanged() {
	}
}



