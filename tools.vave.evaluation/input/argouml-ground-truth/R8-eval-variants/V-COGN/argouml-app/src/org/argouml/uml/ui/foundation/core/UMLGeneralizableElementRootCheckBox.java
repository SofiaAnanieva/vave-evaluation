package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLGeneralizableElementRootCheckBox extends UMLCheckBox2 {
	public UMLGeneralizableElementRootCheckBox() {
		super(Translator.localize("checkbox.root-lc"),ActionSetGeneralizableElementRoot.getInstance(),"isRoot");
	}
	public void buildModel() {
		Object target = getTarget();
		if (target != null&&Model.getFacade().isAUMLElement(target)) {
			setSelected(Model.getFacade().isRoot(target));
		}else {
			setSelected(false);
		}
	}
}



