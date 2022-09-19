package org.argouml.uml.ui.behavior.collaborations;

import java.util.Iterator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLInteractionMessagesListModel extends UMLModelElementListModel2 {
	public UMLInteractionMessagesListModel() {
		super("message");
	}
	protected void buildModelList() {
		removeAllElements();
		Iterator it = Model.getFacade().getMessages(getTarget()).iterator();
		while (it.hasNext()) {
			addElement(it.next());
		}
	}
	protected boolean isValidElement(Object elem) {
		return Model.getFacade().isAMessage(elem)&&Model.getFacade().getInteraction(elem) == getTarget();
	}
}



