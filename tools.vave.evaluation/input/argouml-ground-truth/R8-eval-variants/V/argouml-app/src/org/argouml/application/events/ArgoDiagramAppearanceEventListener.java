package org.argouml.application.events;

import org.argouml.application.api.ArgoEventListener;


public interface ArgoDiagramAppearanceEventListener extends ArgoEventListener {
	public void diagramFontChanged(ArgoDiagramAppearanceEvent e);
}



