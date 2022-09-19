package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;


public class ActionPrint extends AbstractAction {
	public ActionPrint() {
		super(Translator.localize("action.print"),ResourceLoaderWrapper.lookupIcon("action.print"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.print"));
	}
	public void actionPerformed(ActionEvent ae) {
		PrintManager.getInstance().print();
	}
}



