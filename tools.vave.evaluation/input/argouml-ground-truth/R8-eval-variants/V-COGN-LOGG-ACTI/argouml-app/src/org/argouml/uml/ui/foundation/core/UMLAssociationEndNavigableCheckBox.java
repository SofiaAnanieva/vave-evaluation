package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLAssociationEndNavigableCheckBox extends UMLCheckBox2 {
	public UMLAssociationEndNavigableCheckBox() {
		super(Translator.localize("label.navigable"),ActionSetAssociationEndNavigable.getInstance(),"isNavigable");
	}
	public void buildModel() {
		if (getTarget() != null) {
			setSelected(Model.getFacade().isNavigable(getTarget()));
		}
	}
}



