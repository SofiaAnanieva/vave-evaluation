package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInstanceSenderStimulusListModel extends UMLModelElementListModel2 {
	public UMLInstanceSenderStimulusListModel() {
		super("stimulus");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getSentStimuli(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getSentStimuli(getTarget()).contains(element);
	}
}



