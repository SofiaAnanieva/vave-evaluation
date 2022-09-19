package org.argouml.uml.ui.model_management;

import java.awt.event.ActionEvent;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionRemoveElement;


class ActionRemovePackageImport extends AbstractActionRemoveElement {
	ActionRemovePackageImport() {
			super(Translator.localize("menu.popup.remove"));
		}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Model.getModelManagementHelper().removeImportedElement(getTarget(),getObjectToRemove());
	}
}



