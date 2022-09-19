package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionRemoveModelElement;
import org.argouml.uml.ui.behavior.activity_graphs.ActionAddEventAsTrigger;


public class PopupMenuNewEvent extends JPopupMenu {
	public PopupMenuNewEvent(String role,Object target) {
		super();
		buildMenu(this,role,target);
	}
	static void buildMenu(JPopupMenu pmenu,String role,Object target) {
		assert role != null;
		assert target != null;
		if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)||role.equals(ActionNewEvent.Roles.TRIGGER)) {
			JMenu select = new JMenu(Translator.localize("action.select"));
			if (role.equals(ActionNewEvent.Roles.DEFERRABLE_EVENT)) {
				ActionAddEventAsDeferrableEvent.SINGLETON.setTarget(target);
				JMenuItem menuItem = new JMenuItem(ActionAddEventAsDeferrableEvent.SINGLETON);
				select.add(menuItem);
			}else if (role.equals(ActionNewEvent.Roles.TRIGGER)) {
				ActionAddEventAsTrigger.SINGLETON.setTarget(target);
				select.add(ActionAddEventAsTrigger.SINGLETON);
			}
			pmenu.add(select);
		}
		JMenu newMenu = new JMenu(Translator.localize("action.new"));
		newMenu.add(ActionNewCallEvent.getSingleton());
		ActionNewCallEvent.getSingleton().setTarget(target);
		ActionNewCallEvent.getSingleton().putValue(ActionNewEvent.ROLE,role);
		newMenu.add(ActionNewChangeEvent.getSingleton());
		ActionNewChangeEvent.getSingleton().setTarget(target);
		ActionNewChangeEvent.getSingleton().putValue(ActionNewEvent.ROLE,role);
		newMenu.add(ActionNewSignalEvent.getSingleton());
		ActionNewSignalEvent.getSingleton().setTarget(target);
		ActionNewSignalEvent.getSingleton().putValue(ActionNewEvent.ROLE,role);
		newMenu.add(ActionNewTimeEvent.getSingleton());
		ActionNewTimeEvent.getSingleton().setTarget(target);
		ActionNewTimeEvent.getSingleton().putValue(ActionNewEvent.ROLE,role);
		pmenu.add(newMenu);
		pmenu.addSeparator();
		ActionRemoveModelElement.SINGLETON.setObjectToRemove(ActionNewEvent.getAction(role,target));
		ActionRemoveModelElement.SINGLETON.putValue(Action.NAME,Translator.localize("action.delete-from-model"));
		pmenu.add(ActionRemoveModelElement.SINGLETON);
	}
	private static final long serialVersionUID = -7624618103144695448l;
}



