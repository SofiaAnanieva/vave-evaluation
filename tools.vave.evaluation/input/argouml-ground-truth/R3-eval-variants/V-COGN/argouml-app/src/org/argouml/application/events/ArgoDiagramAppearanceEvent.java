package org.argouml.application.events;

import org.argouml.application.events.ArgoEventTypes;
import static org.argouml.application.events.ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT;


public class ArgoDiagramAppearanceEvent extends ArgoEvent {
	public ArgoDiagramAppearanceEvent(int eventType,Object src) {
		super(eventType,src);
	}
	public int getEventStartRange() {
		return ANY_DIAGRAM_APPEARANCE_EVENT;
	}
}



