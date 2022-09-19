package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInstanceReceiverStimulusListModel extends UMLModelElementListModel2 {
	public UMLInstanceReceiverStimulusListModel() {
		super("stimulus");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getReceivedStimuli(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getReceivedStimuli(getTarget()).contains(element);
	}
}



