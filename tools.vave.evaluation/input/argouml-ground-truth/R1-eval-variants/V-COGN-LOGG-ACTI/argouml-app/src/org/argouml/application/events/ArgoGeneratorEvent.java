package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_GENERATOR_EVENT;


public class ArgoGeneratorEvent extends ArgoEvent {
	public ArgoGeneratorEvent(int eventType,Object src) {
		super(eventType,src);
	}
	public int getEventStartRange() {
		return ANY_GENERATOR_EVENT;
	}
}



