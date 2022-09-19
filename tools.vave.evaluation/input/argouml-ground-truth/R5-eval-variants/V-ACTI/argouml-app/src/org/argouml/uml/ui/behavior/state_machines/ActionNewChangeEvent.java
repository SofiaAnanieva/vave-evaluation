package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;


public class ActionNewChangeEvent extends ActionNewEvent {
	private static ActionNewChangeEvent singleton = new ActionNewChangeEvent();
	protected ActionNewChangeEvent() {
		super();
		putValue(NAME,Translator.localize("button.new-changeevent"));
	}
	protected Object createEvent(Object ns) {
		return Model.getStateMachinesFactory().buildChangeEvent(ns);
	}
	public static ActionNewChangeEvent getSingleton() {
		return singleton;
	}
}



