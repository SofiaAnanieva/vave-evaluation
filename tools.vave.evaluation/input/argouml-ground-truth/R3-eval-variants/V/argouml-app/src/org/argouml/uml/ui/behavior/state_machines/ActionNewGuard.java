package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewGuard extends AbstractActionNewModelElement {
	private static ActionNewGuard singleton = new ActionNewGuard();
	protected ActionNewGuard() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionNewGuard getSingleton() {
		return singleton;
	}
	public boolean isEnabled() {
		Object t = getTarget();
		return t != null&&Model.getFacade().getGuard(t) == null;
	}
}



