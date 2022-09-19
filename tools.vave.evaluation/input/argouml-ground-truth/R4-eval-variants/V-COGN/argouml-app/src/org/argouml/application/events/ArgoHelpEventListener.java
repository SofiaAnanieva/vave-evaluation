package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;


public interface ArgoHelpEventListener extends ArgoEventListener {
	public void helpChanged(ArgoHelpEvent e);
	public void helpRemoved(ArgoHelpEvent e);
}



