package org.argouml.uml.diagram.ui;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoStatusEvent;
import org.tigris.gef.ui.IStatusBar;


public class StatusBarAdapter implements IStatusBar {
	public void showStatus(String statusText) {
		ArgoEventPump.fireEvent(new ArgoStatusEvent(ArgoEventTypes.STATUS_TEXT,this,statusText));
	}
}



