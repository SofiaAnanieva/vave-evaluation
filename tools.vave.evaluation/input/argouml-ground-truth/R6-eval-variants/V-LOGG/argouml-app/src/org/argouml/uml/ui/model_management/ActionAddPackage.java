package org.argouml.uml.ui.model_management;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


class ActionAddPackage extends AbstractActionNewModelElement {
	public ActionAddPackage() {
		super("button.new-package");
		putValue(Action.NAME,Translator.localize("button.new-package"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAPackage(target)) {
			Object newPackage = Model.getModelManagementFactory().createPackage();
			Model.getCoreHelper().addOwnedElement(target,newPackage);
			TargetManager.getInstance().setTarget(newPackage);
			super.actionPerformed(e);
		}
	}
}



