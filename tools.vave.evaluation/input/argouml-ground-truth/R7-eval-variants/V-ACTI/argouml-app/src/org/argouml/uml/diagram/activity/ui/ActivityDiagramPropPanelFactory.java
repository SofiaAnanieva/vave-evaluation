package org.argouml.uml.diagram.activity.ui;

import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;


public class ActivityDiagramPropPanelFactory implements PropPanelFactory {
	public PropPanel createPropPanel(Object object) {
		if (object instanceof UMLActivityDiagram) {
			return new PropPanelUMLActivityDiagram();
		}
		return null;
	}
}



