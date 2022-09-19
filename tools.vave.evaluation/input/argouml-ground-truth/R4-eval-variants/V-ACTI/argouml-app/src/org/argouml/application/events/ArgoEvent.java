package org.argouml.application.events;

import java.util.EventObject;
import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_EVENT;
import static org.argouml.application.events.ArgoEventTypes.ARGO_EVENT_END;


public abstract class ArgoEvent extends EventObject implements ArgoEventTypes {
	private int eventType = 0;
	public ArgoEvent(int eT,Object src) {
		super(src);
		eventType = eT;
	}
	public int getEventType() {
		return eventType;
	}
	public int getEventStartRange() {
		return ANY_EVENT;
	}
	public int getEventEndRange() {
		return(getEventStartRange() == 0?ARGO_EVENT_END:getEventStartRange() + 99);
	}
	public String toString() {
		return"{" + getClass().getName() + ":" + eventType + "(" + getEventStartRange() + "-" + getEventEndRange() + ")" + "/" + super.toString() + "}";
	}
}



