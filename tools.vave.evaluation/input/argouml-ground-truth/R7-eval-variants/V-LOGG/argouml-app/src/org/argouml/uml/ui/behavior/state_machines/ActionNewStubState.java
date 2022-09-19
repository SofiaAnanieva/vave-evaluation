package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewStubState extends AbstractActionNewModelElement {
	private static final ActionNewStubState SINGLETON = new ActionNewStubState();
	protected ActionNewStubState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-stubstate"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionNewStubState getInstance() {
		return SINGLETON;
	}
}



