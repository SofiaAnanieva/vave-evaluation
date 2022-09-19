package org.argouml.uml.diagram.state.ui;

import org.argouml.model.Model;


public class ButtonActionNewChangeEvent extends ButtonActionNewEvent {
	protected Object createEvent(Object ns) {
		return Model.getStateMachinesFactory().buildChangeEvent(ns);
	}
	protected String getKeyName() {
		return"button.new-changeevent";
	}
	protected String getIconName() {
		return"ChangeEvent";
	}
}



