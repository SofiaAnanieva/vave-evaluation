package org.argouml.ui.cmd;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.AdjustPageBreaksAction;


public class ActionAdjustPageBreaks extends AdjustPageBreaksAction {
	public ActionAdjustPageBreaks() {
		this(Translator.localize("menu.adjust-pagebreaks"));
	}
	public ActionAdjustPageBreaks(String name) {
		super(name);
	}
}



