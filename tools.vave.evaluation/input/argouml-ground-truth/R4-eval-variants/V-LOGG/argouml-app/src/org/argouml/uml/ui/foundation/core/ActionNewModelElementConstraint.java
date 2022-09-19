package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewModelElementConstraint extends AbstractActionNewModelElement {
	private static final ActionNewModelElementConstraint SINGLETON = new ActionNewModelElementConstraint();
	protected ActionNewModelElementConstraint() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Model.getCoreFactory().buildConstraint(getTarget());
	}
	public static ActionNewModelElementConstraint getInstance() {
		return SINGLETON;
	}
}



