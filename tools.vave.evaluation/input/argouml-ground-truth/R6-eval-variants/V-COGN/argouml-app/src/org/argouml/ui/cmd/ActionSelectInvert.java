package org.argouml.ui.cmd;

import org.argouml.cognitive.Translator;
import org.tigris.gef.base.SelectInvertAction;


public class ActionSelectInvert extends SelectInvertAction {
	public ActionSelectInvert() {
		this(Translator.localize("menu.item.invert-selection"));
	}
	ActionSelectInvert(String name) {
			super(name);
		}
}



