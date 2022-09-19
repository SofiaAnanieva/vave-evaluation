package org.argouml.uml.ui;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;


public class UMLTaggedValueCheckBox extends UMLCheckBox2 {
	private String tagName;
	public UMLTaggedValueCheckBox(String name) {
		super(Translator.localize("checkbox." + name + "-lc"),new ActionBooleanTaggedValue(name),name);
		tagName = name;
	}
	public void buildModel() {
		Object tv = Model.getFacade().getTaggedValue(getTarget(),tagName);
		if (tv != null) {
			String tag = Model.getFacade().getValueOfTag(tv);
			if ("true".equals(tag)) {
				setSelected(true);
				return;
			}
		}
		setSelected(false);
	}
}



