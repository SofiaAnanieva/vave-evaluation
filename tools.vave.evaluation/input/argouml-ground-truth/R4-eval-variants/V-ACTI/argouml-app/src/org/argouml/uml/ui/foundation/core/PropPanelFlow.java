package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.foundation.core.PropPanelRelationship;


public class PropPanelFlow extends PropPanelRelationship {
	private static final long serialVersionUID = 2967789232647658450l;
	public PropPanelFlow() {
		super("label.flow",lookupIcon("Flow"));
		initialize();
	}
	private void initialize() {
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addField(Translator.localize("label.constraints"),getConstraintScroll());
		addSeparator();
	}
}



