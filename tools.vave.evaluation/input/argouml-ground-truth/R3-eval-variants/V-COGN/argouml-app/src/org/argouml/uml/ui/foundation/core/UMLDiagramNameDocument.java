package org.argouml.uml.ui.foundation.core;

import java.beans.PropertyVetoException;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.ui.UMLPlainTextDocument;


public class UMLDiagramNameDocument extends UMLPlainTextDocument {
	public UMLDiagramNameDocument() {
		super("name");
	}
	protected void setProperty(String text) {
		Object target = DiagramUtils.getActiveDiagram();
		if (target instanceof ArgoDiagram) {
			try {
				((ArgoDiagram) target).setName(text);
			}catch (PropertyVetoException e) {
			}
		}
	}
	protected String getProperty() {
		Object target = DiagramUtils.getActiveDiagram();
		if (target instanceof ArgoDiagram) {
			return((ArgoDiagram) target).getName();
		}
		return"";
	}
}



