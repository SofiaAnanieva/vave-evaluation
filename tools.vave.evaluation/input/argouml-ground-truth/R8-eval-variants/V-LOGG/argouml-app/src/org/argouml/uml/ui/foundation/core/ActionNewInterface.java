package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


class ActionNewInterface extends AbstractActionNewModelElement {
	public ActionNewInterface() {
		super("button.new-interface");
		putValue(Action.NAME,Translator.localize("button.new-interface"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAInterface(target)) {
			Object iface = target;
			Object newInterface = Model.getCoreFactory().createInterface();
			Model.getCoreHelper().addOwnedElement(Model.getFacade().getNamespace(iface),newInterface);
			TargetManager.getInstance().setTarget(newInterface);
			super.actionPerformed(e);
		}
	}
}



