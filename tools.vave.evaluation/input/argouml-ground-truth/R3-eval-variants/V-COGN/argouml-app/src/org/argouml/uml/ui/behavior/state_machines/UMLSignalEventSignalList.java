package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPopupMenu;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSignal;
import org.argouml.uml.ui.behavior.state_machines.ActionAddSignalsToSignalEvent;


class UMLSignalEventSignalList extends UMLMutableLinkedList {
	public UMLSignalEventSignalList(UMLModelElementListModel2 dataModel) {
		super(dataModel,(AbstractActionAddModelElement2) null,null,null,true);
		setDelete(false);
		setDeleteAction(null);
	}
	public JPopupMenu getPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		ActionAddSignalsToSignalEvent.SINGLETON.setTarget(getTarget());
		menu.add(ActionAddSignalsToSignalEvent.SINGLETON);
		menu.add(new ActionNewSignal());
		return menu;
	}
}



