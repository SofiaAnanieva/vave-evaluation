package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class ActionNewCallEvent extends ActionNewEvent {
	private static ActionNewCallEvent singleton = new ActionNewCallEvent();
	protected ActionNewCallEvent() {
		super();
		putValue(NAME,Translator.localize("button.new-callevent"));
	}
	protected Object createEvent(Object ns) {
		return Model.getStateMachinesFactory().buildCallEvent(ns);
	}
	public static ActionNewCallEvent getSingleton() {
		return singleton;
	}
}



