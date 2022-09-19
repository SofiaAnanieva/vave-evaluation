package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewArgument extends AbstractActionNewModelElement {
	public ActionNewArgument() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object target = getTarget();
		if (Model.getFacade().isAAction(target)) {
			Object argument = Model.getCommonBehaviorFactory().createArgument();
			Model.getCommonBehaviorHelper().addActualArgument(target,argument);
			TargetManager.getInstance().setTarget(argument);
		}
	}
}



