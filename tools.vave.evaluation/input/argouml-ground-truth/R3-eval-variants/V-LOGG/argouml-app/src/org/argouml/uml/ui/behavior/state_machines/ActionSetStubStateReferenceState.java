package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;


public class ActionSetStubStateReferenceState extends UndoableAction {
	private static final ActionSetStubStateReferenceState SINGLETON = new ActionSetStubStateReferenceState();
	protected ActionSetStubStateReferenceState() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionSetStubStateReferenceState getInstance() {
		return SINGLETON;
	}
}



