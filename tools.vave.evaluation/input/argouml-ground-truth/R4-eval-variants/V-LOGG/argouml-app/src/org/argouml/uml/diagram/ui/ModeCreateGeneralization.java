package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.ModeCreateGraphEdge;


public final class ModeCreateGeneralization extends ModeCreateGraphEdge {
	protected final Object getMetaType() {
		return Model.getMetaTypes().getGeneralization();
	}
}



