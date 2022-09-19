package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewException extends AbstractActionNewModelElement {
	public ActionNewException() {
		super("button.new-exception");
		putValue(Action.NAME,Translator.localize("button.new-exception"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		Object ns = null;
		if (Model.getFacade().isANamespace(target)) {
			ns = target;
		}else {
			ns = Model.getFacade().getNamespace(target);
		}
		Object newElement = Model.getCommonBehaviorFactory().createException();
		Model.getCoreHelper().setNamespace(newElement,ns);
		TargetManager.getInstance().setTarget(newElement);
		super.actionPerformed(e);
	}
}



