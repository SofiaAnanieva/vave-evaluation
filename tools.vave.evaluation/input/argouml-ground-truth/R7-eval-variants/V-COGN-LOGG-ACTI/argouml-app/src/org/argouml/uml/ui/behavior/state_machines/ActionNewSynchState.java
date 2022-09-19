package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewSynchState extends AbstractActionNewModelElement {
	private static final ActionNewSynchState SINGLETON = new ActionNewSynchState();
	protected ActionNewSynchState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-synchstate"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Model.getStateMachinesFactory().buildSynchState(getTarget());
	}
	public static ActionNewSynchState getInstance() {
		return SINGLETON;
	}
}



