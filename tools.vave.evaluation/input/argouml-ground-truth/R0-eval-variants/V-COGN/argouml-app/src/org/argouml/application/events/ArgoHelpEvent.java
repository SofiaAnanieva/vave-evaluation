package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_HELP_EVENT;


public class ArgoHelpEvent extends ArgoEvent {
	private String helpText;
	public ArgoHelpEvent(int eventType,Object src,String message) {
		super(eventType,src);
		helpText = message;
	}
	@Override public int getEventStartRange() {
		return ANY_HELP_EVENT;
	}
	public String getHelpText() {
		return helpText;
	}
}



