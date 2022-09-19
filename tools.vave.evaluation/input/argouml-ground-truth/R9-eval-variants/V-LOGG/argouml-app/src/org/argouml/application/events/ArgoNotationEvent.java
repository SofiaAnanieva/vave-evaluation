package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_NOTATION_EVENT;


public class ArgoNotationEvent extends ArgoEvent {
	public ArgoNotationEvent(int eventType,Object src) {
		super(eventType,src);
	}
	public int getEventStartRange() {
		return ANY_NOTATION_EVENT;
	}
}



