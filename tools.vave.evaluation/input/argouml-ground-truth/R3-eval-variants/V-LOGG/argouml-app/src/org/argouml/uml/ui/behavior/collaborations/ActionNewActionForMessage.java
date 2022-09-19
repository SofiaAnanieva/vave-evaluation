package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewActionForMessage extends AbstractActionNewModelElement {
	private static final ActionNewActionForMessage SINGLETON = new ActionNewActionForMessage();
	public ActionNewActionForMessage() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Model.getCommonBehaviorFactory().buildAction(getTarget());
	}
	public boolean isEnabled() {
		if (getTarget() != null) {
			return Model.getFacade().getAction(getTarget()) == null;
		}
		return false;
	}
	public static ActionNewActionForMessage getInstance() {
		return SINGLETON;
	}
}



