package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationEndNavigable extends UndoableAction {
	private static final ActionSetAssociationEndNavigable SINGLETON = new ActionSetAssociationEndNavigable();
	protected ActionSetAssociationEndNavigable() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAAssociationEnd(target)) {
				Object m = target;
				Model.getCoreHelper().setNavigable(m,source.isSelected());
			}
		}
	}
	public static ActionSetAssociationEndNavigable getInstance() {
		return SINGLETON;
	}
}



