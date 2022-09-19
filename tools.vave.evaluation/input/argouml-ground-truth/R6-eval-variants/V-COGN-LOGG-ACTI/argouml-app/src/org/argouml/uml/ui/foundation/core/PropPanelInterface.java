package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelInterface extends PropPanelClassifier {
	private static final long serialVersionUID = 849399652073446108l;
	public PropPanelInterface() {
		super("label.interface",lookupIcon("Interface"));
		addField(Translator.localize("label.name"),getNameTextField());
		addField(Translator.localize("label.namespace"),getNamespaceSelector());
		add(getModifiersPanel());
		add(getVisibilityPanel());
		addSeparator();
		addField(Translator.localize("label.generalizations"),getGeneralizationScroll());
		addField(Translator.localize("label.specializations"),getSpecializationScroll());
		addSeparator();
		addField(Translator.localize("label.association-ends"),getAssociationEndScroll());
		addField(Translator.localize("label.features"),getFeatureScroll());
		addAction(new ActionNavigateNamespace());
		addAction(new ActionAddOperation());
		addAction(getActionNewReception());
		addAction(new ActionNewInterface());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



