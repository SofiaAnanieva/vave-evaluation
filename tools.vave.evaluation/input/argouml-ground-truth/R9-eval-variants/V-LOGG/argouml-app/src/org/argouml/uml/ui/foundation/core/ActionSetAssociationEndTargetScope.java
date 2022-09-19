package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationEndTargetScope extends UndoableAction {
	private static final ActionSetAssociationEndTargetScope SINGLETON = new ActionSetAssociationEndTargetScope();
	protected ActionSetAssociationEndTargetScope() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAAssociationEnd(target)) {
				Object m = target;
				Model.getCoreHelper().setStatic(m,source.isSelected());
			}
		}
	}
	public static ActionSetAssociationEndTargetScope getInstance() {
		return SINGLETON;
	}
}



