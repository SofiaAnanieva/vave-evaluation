package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;


public interface ArgoNotationEventListener extends ArgoEventListener {
	public void notationChanged(ArgoNotationEvent e);
	public void notationAdded(ArgoNotationEvent e);
	public void notationRemoved(ArgoNotationEvent e);
	public void notationProviderAdded(ArgoNotationEvent e);
	public void notationProviderRemoved(ArgoNotationEvent e);
}



