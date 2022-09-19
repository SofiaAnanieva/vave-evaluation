package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.foundation.core.PropPanelDependency;


public class PropPanelUsage extends PropPanelDependency {
	private static final long serialVersionUID = 5927912703376526760l;
	public PropPanelUsage() {
		super("label.usage",lookupIcon("Usage"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.suppliers"),getSupplierScroll());
		addField(Translator.localize("label.clients"),getClientScroll());
		addAction(new ActionNavigateNamespace());
		addAction(getDeleteAction());
	}
}



