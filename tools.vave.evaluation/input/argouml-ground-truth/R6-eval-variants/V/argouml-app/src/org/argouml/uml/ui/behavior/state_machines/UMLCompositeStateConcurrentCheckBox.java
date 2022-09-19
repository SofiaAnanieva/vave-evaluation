package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


@Deprecated public class UMLCompositeStateConcurrentCheckBox extends UMLCheckBox2 {
	public UMLCompositeStateConcurrentCheckBox() {
		super(Translator.localize("label.concurrent"),ActionSetCompositeStateConcurrent.getInstance(),"isConcurent");
	}
	public void buildModel() {
		setSelected(Model.getFacade().isConcurrent(getTarget()));
	}
}



