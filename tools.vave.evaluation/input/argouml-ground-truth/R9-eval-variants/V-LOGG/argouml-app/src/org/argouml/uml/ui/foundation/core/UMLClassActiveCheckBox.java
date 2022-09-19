package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLClassActiveCheckBox extends UMLCheckBox2 {
	public UMLClassActiveCheckBox() {
		super(Translator.localize("checkbox.active-lc"),ActionSetClassActive.getInstance(),"isActive");
	}
	public void buildModel() {
		if (getTarget() != null) {
			setSelected(Model.getFacade().isActive(getTarget()));
		}
	}
}



