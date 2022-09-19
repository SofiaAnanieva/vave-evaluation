package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;


public interface ArgoProfileEventListener extends ArgoEventListener {
	public void profileAdded(ArgoProfileEvent e);
	public void profileRemoved(ArgoProfileEvent e);
}



