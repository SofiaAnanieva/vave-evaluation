package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.ModeCreateDependency;


public final class ModeCreateUsage extends ModeCreateDependency {
	protected final Object getMetaType() {
		return Model.getMetaTypes().getUsage();
	}
}



