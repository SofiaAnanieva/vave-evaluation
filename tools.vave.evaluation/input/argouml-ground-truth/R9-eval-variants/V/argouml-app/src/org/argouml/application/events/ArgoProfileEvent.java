package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_PROFILE_EVENT;


public class ArgoProfileEvent extends ArgoEvent {
	public ArgoProfileEvent(int eT,Object src) {
		super(eT,src);
	}
	public int getEventStartRange() {
		return ANY_PROFILE_EVENT;
	}
}



