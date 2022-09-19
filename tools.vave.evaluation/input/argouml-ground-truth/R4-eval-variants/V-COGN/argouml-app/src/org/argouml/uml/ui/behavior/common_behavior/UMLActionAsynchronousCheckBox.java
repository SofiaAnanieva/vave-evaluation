package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLActionAsynchronousCheckBox extends UMLCheckBox2 {
	public UMLActionAsynchronousCheckBox() {
		super(Translator.localize("checkbox.asynchronous"),ActionSetActionAsynchronous.getInstance(),"isAsynchronous");
	}
	public void buildModel() {
		if (getTarget() != null) {
			setSelected(Model.getFacade().isAsynchronous(getTarget()));
		}
	}
}



