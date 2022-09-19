package org.argouml.uml.ui.foundation.core;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLGeneralizableElementLeafCheckBox extends UMLCheckBox2 {
	public UMLGeneralizableElementLeafCheckBox() {
		super(Translator.localize("checkbox.leaf-lc"),ActionSetGeneralizableElementLeaf.getInstance(),"isLeaf");
	}
	public void buildModel() {
		Object target = getTarget();
		if (target != null&&Model.getFacade().isAUMLElement(target)) {
			setSelected(Model.getFacade().isLeaf(target));
		}else {
			setSelected(false);
		}
	}
}



