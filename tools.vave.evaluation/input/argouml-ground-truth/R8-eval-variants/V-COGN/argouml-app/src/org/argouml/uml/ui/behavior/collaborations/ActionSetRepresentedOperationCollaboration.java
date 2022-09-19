package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


class ActionSetRepresentedOperationCollaboration extends UndoableAction {
	ActionSetRepresentedOperationCollaboration() {
			super(Translator.localize("action.set"),ResourceLoaderWrapper.lookupIcon("action.set"));
		}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLComboBox2) {
			UMLComboBox2 source = (UMLComboBox2) e.getSource();
			Object target = source.getTarget();
			Object newValue = source.getSelectedItem();
			if (!Model.getFacade().isAOperation(newValue)) {
				newValue = null;
			}
		}
	}
}



