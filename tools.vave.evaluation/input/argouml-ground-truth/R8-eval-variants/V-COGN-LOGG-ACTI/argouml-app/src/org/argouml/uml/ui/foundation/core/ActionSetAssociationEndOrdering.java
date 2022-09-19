package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationEndOrdering extends UndoableAction {
	private static final ActionSetAssociationEndOrdering SINGLETON = new ActionSetAssociationEndOrdering();
	protected ActionSetAssociationEndOrdering() {
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
				if (source.isSelected()) {
					Model.getCoreHelper().setOrdering(m,Model.getOrderingKind().getOrdered());
				}else {
					Model.getCoreHelper().setOrdering(m,Model.getOrderingKind().getUnordered());
				}
			}
		}
	}
	public static ActionSetAssociationEndOrdering getInstance() {
		return SINGLETON;
	}
}



