package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLGeneralizableElementAbstractCheckBox extends UMLCheckBox2 {
	public UMLGeneralizableElementAbstractCheckBox() {
		super(Translator.localize("checkbox.abstract-lc"),ActionSetGeneralizableElementAbstract.getInstance(),"isAbstract");
	}
	public void buildModel() {
		Object target = getTarget();
		if (target != null&&Model.getFacade().isAUMLElement(target)) {
			setSelected(Model.getFacade().isAbstract(target));
		}else {
			setSelected(false);
		}
	}
}



