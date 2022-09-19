package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewAction;
import org.argouml.uml.ui.behavior.common_behavior.PopupMenuNewAction;


public class UMLStateDoActivityList extends UMLMutableLinkedList {
	public UMLStateDoActivityList(UMLModelElementListModel2 dataModel) {
		super(dataModel);
	}
	public JPopupMenu getPopupMenu() {
		return new PopupMenuNewAction(ActionNewAction.Roles.DO,this);
	}
}



