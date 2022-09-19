package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetContextStateMachine extends UndoableAction {
	private static final ActionSetContextStateMachine SINGLETON = new ActionSetContextStateMachine();
	protected ActionSetContextStateMachine() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLComboBox2) {
			UMLComboBox2 source = (UMLComboBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().getContext(target) != source.getSelectedItem()) {
				Model.getStateMachinesHelper().setContext(target,source.getSelectedItem());
			}
		}
	}
	public static ActionSetContextStateMachine getInstance() {
		return SINGLETON;
	}
	private static final long serialVersionUID = -8118983979324112900l;
}



