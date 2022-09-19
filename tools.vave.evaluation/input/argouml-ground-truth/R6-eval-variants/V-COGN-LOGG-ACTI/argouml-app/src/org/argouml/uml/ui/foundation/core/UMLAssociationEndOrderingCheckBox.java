package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLAssociationEndOrderingCheckBox extends UMLCheckBox2 {
	public UMLAssociationEndOrderingCheckBox() {
		super(Translator.localize("label.ordered"),ActionSetAssociationEndOrdering.getInstance(),"ordering");
	}
	public void buildModel() {
		if (getTarget() != null) {
			Object associationEnd = getTarget();
			setSelected(Model.getOrderingKind().getOrdered().equals(Model.getFacade().getOrdering(associationEnd)));
		}
	}
}



