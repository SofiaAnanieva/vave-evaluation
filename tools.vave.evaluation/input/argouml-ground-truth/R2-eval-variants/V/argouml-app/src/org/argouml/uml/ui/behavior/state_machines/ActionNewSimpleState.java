package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewSimpleState extends AbstractActionNewModelElement {
	private static ActionNewSimpleState singleton = new ActionNewSimpleState();
	protected ActionNewSimpleState() {
		super();
		putValue(Action.NAME,Translator.localize("button.new-simplestate"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}
	public static ActionNewSimpleState getSingleton() {
		return singleton;
	}
}



