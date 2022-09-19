package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.UMLMutableLinkedList;


public class PopupMenuNewAction extends JPopupMenu {
	public PopupMenuNewAction(String role,UMLMutableLinkedList list) {
		super();
		buildMenu(this,role,list.getTarget());
	}
	public static void buildMenu(JPopupMenu pmenu,String role,Object target) {
		JMenu newMenu = new JMenu();
		newMenu.setText(Translator.localize("action.new"));
		newMenu.add(ActionNewCallAction.getInstance());
		ActionNewCallAction.getInstance().setTarget(target);
		ActionNewCallAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewCreateAction.getInstance());
		ActionNewCreateAction.getInstance().setTarget(target);
		ActionNewCreateAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewDestroyAction.getInstance());
		ActionNewDestroyAction.getInstance().setTarget(target);
		ActionNewDestroyAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewReturnAction.getInstance());
		ActionNewReturnAction.getInstance().setTarget(target);
		ActionNewReturnAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewSendAction.getInstance());
		ActionNewSendAction.getInstance().setTarget(target);
		ActionNewSendAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewTerminateAction.getInstance());
		ActionNewTerminateAction.getInstance().setTarget(target);
		ActionNewTerminateAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewUninterpretedAction.getInstance());
		ActionNewUninterpretedAction.getInstance().setTarget(target);
		ActionNewUninterpretedAction.getInstance().putValue(ActionNewAction.ROLE,role);
		newMenu.add(ActionNewActionSequence.getInstance());
		ActionNewActionSequence.getInstance().setTarget(target);
		ActionNewActionSequence.getInstance().putValue(ActionNewAction.ROLE,role);
		pmenu.add(newMenu);
		pmenu.addSeparator();
		ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewAction.getAction(role,target));
		ActionRemoveModelElement.SINGLETON.putValue(Action.NAME,Translator.localize("action.delete-from-model"));
		pmenu.add(ActionRemoveModelElement.SINGLETON);
	}
}



