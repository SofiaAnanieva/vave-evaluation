package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;


public interface ArgoGeneratorEventListener extends ArgoEventListener {
	public void generatorChanged(ArgoGeneratorEvent e);
	public void generatorAdded(ArgoGeneratorEvent e);
	public void generatorRemoved(ArgoGeneratorEvent e);
}



