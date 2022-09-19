package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLAssociationEndTargetScopeCheckbox extends UMLCheckBox2 {
	public UMLAssociationEndTargetScopeCheckbox() {
		super(Translator.localize("label.static"),ActionSetAssociationEndTargetScope.getInstance(),"targetScope");
	}
	public void buildModel() {
		if (getTarget() != null) {
			Object associationEnd = getTarget();
			setSelected(Model.getFacade().isStatic(associationEnd));
		}
	}
}



