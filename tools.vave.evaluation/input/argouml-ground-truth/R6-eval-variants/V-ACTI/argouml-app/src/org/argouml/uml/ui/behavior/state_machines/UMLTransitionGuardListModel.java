package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLTransitionGuardListModel extends UMLModelElementListModel2 {
	public UMLTransitionGuardListModel() {
		super("guard");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getGuard(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getGuard(getTarget());
	}
	@Override public boolean buildPopup(JPopupMenu popup,int index) {
		AbstractActionNewModelElement a = ActionNewGuard.getSingleton();
		a.setTarget(TargetManager.getInstance().getTarget());
		popup.add(a);
		return true;
	}
	@Override protected boolean hasPopup() {
		return true;
	}
}



