package org.argouml.uml.ui.behavior.state_machines;

import java.util.ArrayList;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLStateVertexIncomingListModel extends UMLModelElementListModel2 {
	public UMLStateVertexIncomingListModel() {
		super("incoming");
	}
	protected void buildModelList() {
		ArrayList c = new ArrayList(Model.getFacade().getIncomings(getTarget()));
		if (Model.getFacade().isAState(getTarget())) {
			ArrayList i = new ArrayList(Model.getFacade().getInternalTransitions(getTarget()));
			c.removeAll(i);
		}
		setAllElements(c);
	}
	protected boolean isValidElement(Object element) {
		ArrayList c = new ArrayList(Model.getFacade().getIncomings(getTarget()));
		if (Model.getFacade().isAState(getTarget())) {
			ArrayList i = new ArrayList(Model.getFacade().getInternalTransitions(getTarget()));
			c.removeAll(i);
		}
		return c.contains(element);
	}
}



