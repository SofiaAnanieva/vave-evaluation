package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;


public class ActionAddPackage extends UndoableAction {
	public ActionAddPackage() {
		super(Translator.localize("action.add-package"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object namespace = TargetManager.getInstance().getModelTarget();
		Model.getCoreHelper().addOwnedElement(namespace,Model.getModelManagementFactory().createPackage());
	}
}



