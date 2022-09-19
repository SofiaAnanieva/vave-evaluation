package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetElementOwnershipSpecification extends UndoableAction {
	private static final ActionSetElementOwnershipSpecification SINGLETON = new ActionSetElementOwnershipSpecification();
	protected ActionSetElementOwnershipSpecification() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAModelElement(target)||Model.getFacade().isAElementImport(target)) {
				Object m = target;
				Model.getModelManagementHelper().setSpecification(m,!Model.getFacade().isSpecification(m));
			}
		}
	}
	public static ActionSetElementOwnershipSpecification getInstance() {
		return SINGLETON;
	}
}



