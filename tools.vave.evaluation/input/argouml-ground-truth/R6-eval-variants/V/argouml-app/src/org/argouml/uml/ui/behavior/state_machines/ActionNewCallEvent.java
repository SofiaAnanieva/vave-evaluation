package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class ActionNewCallEvent extends ActionNewEvent {
	private static ActionNewCallEvent singleton = new ActionNewCallEvent();
	protected ActionNewCallEvent() {
		super();
		putValue(NAME,Translator.localize("button.new-callevent"));
	}
	public static ActionNewCallEvent getSingleton() {
		return singleton;
	}
}



