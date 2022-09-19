package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetSubmachineStateSubmachine extends UndoableAction {
	private static final ActionSetSubmachineStateSubmachine SINGLETON = new ActionSetSubmachineStateSubmachine();
	protected ActionSetSubmachineStateSubmachine() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionSetSubmachineStateSubmachine getInstance() {
		return SINGLETON;
	}
}



