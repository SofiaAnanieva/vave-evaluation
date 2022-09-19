package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewFinalState extends AbstractActionNewModelElement {
	private static ActionNewFinalState singleton = new ActionNewFinalState();
	protected ActionNewFinalState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-finalstate"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Model.getStateMachinesFactory().buildFinalState(getTarget());
	}
	public static ActionNewFinalState getSingleton() {
		return singleton;
	}
}



