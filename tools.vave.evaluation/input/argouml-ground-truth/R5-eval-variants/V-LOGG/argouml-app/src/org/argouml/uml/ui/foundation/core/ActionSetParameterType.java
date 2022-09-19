package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetParameterType extends UndoableAction {
	private static final ActionSetParameterType SINGLETON = new ActionSetParameterType();
	protected ActionSetParameterType() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object oldClassifier = null;
		Object newClassifier = null;
		Object para = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = ((UMLComboBox2) source);
			Object o = box.getTarget();
			if (Model.getFacade().isAParameter(o)) {
				para = o;
				oldClassifier = Model.getFacade().getType(para);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isAClassifier(o)) {
				newClassifier = o;
			}
		}
		if (newClassifier != null&&newClassifier != oldClassifier&&para != null) {
			Model.getCoreHelper().setType(para,newClassifier);
			super.actionPerformed(e);
		}
	}
	public static ActionSetParameterType getInstance() {
		return SINGLETON;
	}
}



