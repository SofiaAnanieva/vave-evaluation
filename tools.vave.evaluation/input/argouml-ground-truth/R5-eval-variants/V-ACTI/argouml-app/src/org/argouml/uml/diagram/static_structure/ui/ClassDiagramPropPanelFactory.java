package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;


public class ClassDiagramPropPanelFactory implements PropPanelFactory {
	public PropPanel createPropPanel(Object object) {
		if (object instanceof UMLClassDiagram) {
			return new PropPanelUMLClassDiagram();
		}
		return null;
	}
}



