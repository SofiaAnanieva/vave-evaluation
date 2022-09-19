package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetGeneralizationPowertype extends UndoableAction {
	private static final ActionSetGeneralizationPowertype SINGLETON = new ActionSetGeneralizationPowertype();
	protected ActionSetGeneralizationPowertype() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	@Override public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		Object oldClassifier = null;
		Object newClassifier = null;
		Object gen = null;
		if (source instanceof UMLComboBox2) {
			UMLComboBox2 box = (UMLComboBox2) source;
			Object o = box.getTarget();
			if (Model.getFacade().isAGeneralization(o)) {
				gen = o;
				oldClassifier = Model.getFacade().getPowertype(gen);
			}
			o = box.getSelectedItem();
			if (Model.getFacade().isAClassifier(o)) {
				newClassifier = o;
			}
		}
		if (newClassifier != oldClassifier&&gen != null) {
			Model.getCoreHelper().setPowertype(gen,newClassifier);
		}
	}
	public static ActionSetGeneralizationPowertype getInstance() {
		return SINGLETON;
	}
}



