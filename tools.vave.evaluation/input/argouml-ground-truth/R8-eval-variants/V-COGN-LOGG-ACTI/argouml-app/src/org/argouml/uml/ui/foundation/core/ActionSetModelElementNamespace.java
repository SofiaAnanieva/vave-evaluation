package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetModelElementNamespace extends UndoableAction {
	public ActionSetModelElementNamespace() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		Object oldNamespace = null;
		Object newNamespace = null;
		Object m = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object o = box.getTarget();
			if (Model.getFacade().isAModelElement(o)) {
				m = o;
				oldNamespace = Model.getFacade().getNamespace(m);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isANamespace(o)) {
				newNamespace = o;
			}
		}
		if (newNamespace != oldNamespace&&m != null&&newNamespace != null) {
			super.actionPerformed(e);
			Model.getCoreHelper().setNamespace(m,newNamespace);
		}
	}
}



