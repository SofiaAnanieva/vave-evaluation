package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewTransition extends AbstractActionNewModelElement {
	public static final String SOURCE = "source";
	public static final String DESTINATION = "destination";
	public ActionNewTransition() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public boolean isEnabled() {
		Object target = TargetManager.getInstance().getModelTarget();
		return super.isEnabled();
	}
}



