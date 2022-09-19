package org.argouml.uml.diagram.state.ui;

import org.argouml.model.Model;


public class ButtonActionNewCallEvent extends ButtonActionNewEvent {
	protected Object createEvent(Object ns) {
		return Model.getStateMachinesFactory().buildCallEvent(ns);
	}
	protected String getKeyName() {
		return"button.new-callevent";
	}
	protected String getIconName() {
		return"CallEvent";
	}
}



