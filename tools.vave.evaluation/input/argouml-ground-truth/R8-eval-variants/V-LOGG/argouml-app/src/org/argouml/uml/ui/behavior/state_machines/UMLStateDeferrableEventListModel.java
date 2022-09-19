package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateDeferrableEventListModel extends UMLModelElementListModel2 {
	public UMLStateDeferrableEventListModel() {
		super("deferrableEvent");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getDeferrableEvents(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getDeferrableEvents(getTarget()).contains(element);
	}
	@Override public boolean buildPopup(JPopupMenu popup,int index) {
		PopupMenuNewEvent.buildMenu(popup,ActionNewEvent.Roles.DEFERRABLE_EVENT,getTarget());
		return true;
	}
}



