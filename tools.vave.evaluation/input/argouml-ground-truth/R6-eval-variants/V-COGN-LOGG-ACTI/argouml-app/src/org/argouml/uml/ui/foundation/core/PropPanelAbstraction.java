package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.foundation.core.PropPanelDependency;


public class PropPanelAbstraction extends PropPanelDependency {
	private static final long serialVersionUID = 595724551744206773l;
	public PropPanelAbstraction() {
		super("label.abstraction",lookupIcon("Abstraction"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		addSeparator();
		addField(Translator.localize("label.suppliers"),getSupplierScroll());
		addField(Translator.localize("label.clients"),getClientScroll());
		addAction(new ActionNavigateNamespace());
		addAction(getDeleteAction());
	}
}



