package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.ModeCreateGraphEdge;


public class ModeCreateDependency extends ModeCreateGraphEdge {
	protected Object getMetaType() {
		return Model.getMetaTypes().getDependency();
	}
}



