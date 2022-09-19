package org.argouml.ui.cmd;

import org.argouml.cognitive.Translator;
import org.tigris.gef.base.SelectAllAction;


public class ActionSelectAll extends SelectAllAction {
	public ActionSelectAll() {
		this(Translator.localize("menu.item.select-all"));
	}
	ActionSelectAll(String name) {
			super(name);
		}
}



