package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewAction;
import org.argouml.uml.ui.behavior.common_behavior.PopupMenuNewAction;


public class UMLTransitionEffectListModel extends UMLModelElementListModel2 {
	public UMLTransitionEffectListModel() {
		super("effect");
	}
	protected void buildModelList() {
		removeAllElements();
		addElement(Model.getFacade().getEffect(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return element == Model.getFacade().getEffect(getTarget());
	}
	@Override public boolean buildPopup(JPopupMenu popup,int index) {
		PopupMenuNewAction.buildMenu(popup,ActionNewAction.Roles.EFFECT,getTarget());
		return true;
	}
	@Override protected boolean hasPopup() {
		return true;
	}
}



