package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLFeatureOwnerScopeCheckBox extends UMLCheckBox2 {
	public UMLFeatureOwnerScopeCheckBox() {
		super(Translator.localize("checkbox.static-lc"),ActionSetFeatureOwnerScope.getInstance(),"ownerScope");
	}
	public void buildModel() {
		setSelected(Model.getFacade().isStatic(getTarget()));
	}
}



