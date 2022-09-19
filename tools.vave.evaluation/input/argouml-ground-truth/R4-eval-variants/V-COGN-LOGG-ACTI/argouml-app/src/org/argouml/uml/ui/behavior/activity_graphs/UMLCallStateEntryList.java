package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;


class UMLCallStateEntryList extends UMLMutableLinkedList {
	public UMLCallStateEntryList(UMLModelElementListModel2 dataModel) {
		super(dataModel);
	}
	public JPopupMenu getPopupMenu() {
		return new PopupMenuNewCallAction(ActionNewAction.Roles.ENTRY,this);
	}
	static class PopupMenuNewCallAction extends JPopupMenu {
	public PopupMenuNewCallAction(String role,UMLMutableLinkedList list) {
		super();
		JMenu newMenu = new JMenu();
		newMenu.setText(Translator.localize("action.new"));
		newMenu.add(ActionNewCallAction.getInstance());
		ActionNewCallAction.getInstance().setTarget(list.getTarget());
		ActionNewCallAction.getInstance().putValue(ActionNewAction.ROLE,role);
		add(newMenu);
		addSeparator();
		ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewAction.getAction(role,list.getTarget()));
		add(ActionRemoveModelElement.SINGLETON);
	}
}
}



