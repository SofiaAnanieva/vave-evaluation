package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewSubmachineState extends AbstractActionNewModelElement {
	private static final ActionNewSubmachineState SINGLETON = new ActionNewSubmachineState();
	protected ActionNewSubmachineState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-submachinestate"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionNewSubmachineState getInstance() {
		return SINGLETON;
	}
}



