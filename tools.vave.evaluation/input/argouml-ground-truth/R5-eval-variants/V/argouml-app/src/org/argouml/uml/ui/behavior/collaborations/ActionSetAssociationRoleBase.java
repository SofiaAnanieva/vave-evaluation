package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationRoleBase extends UndoableAction {
	public ActionSetAssociationRoleBase() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLComboBox2) {
			UMLComboBox2 source = (UMLComboBox2) e.getSource();
			Object assoc = source.getSelectedItem();
			Object ar = source.getTarget();
			if (Model.getFacade().getBase(ar) == assoc) {
				return;
			}
		}
	}
}



