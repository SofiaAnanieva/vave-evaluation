package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetAssociationEndType extends UndoableAction {
	private static final ActionSetAssociationEndType SINGLETON = new ActionSetAssociationEndType();
	protected ActionSetAssociationEndType() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object oldClassifier = null;
		Object newClassifier = null;
		Object end = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object o = box.getTarget();
			if (Model.getFacade().isAAssociationEnd(o)) {
				end = o;
				oldClassifier = Model.getFacade().getType(end);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isAClassifier(o)) {
				newClassifier = o;
			}
		}
		if (newClassifier != oldClassifier&&end != null&&newClassifier != null) {
			Model.getCoreHelper().setType(end,newClassifier);
			super.actionPerformed(e);
		}
	}
	public static ActionSetAssociationEndType getInstance() {
		return SINGLETON;
	}
}



