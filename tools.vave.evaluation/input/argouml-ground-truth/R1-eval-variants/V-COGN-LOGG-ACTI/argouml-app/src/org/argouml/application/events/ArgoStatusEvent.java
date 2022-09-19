package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_STATUS_EVENT;


public class ArgoStatusEvent extends ArgoEvent {
	private String text;
	public ArgoStatusEvent(int eventType,Object src,String message) {
		super(eventType,src);
		text = message;
	}
	@Override public int getEventStartRange() {
		return ANY_STATUS_EVENT;
	}
	public String getText() {
		return text;
	}
}



