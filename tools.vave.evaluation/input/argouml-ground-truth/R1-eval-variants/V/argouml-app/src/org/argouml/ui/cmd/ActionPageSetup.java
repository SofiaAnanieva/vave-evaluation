package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;


class ActionPageSetup extends AbstractAction {
	public ActionPageSetup() {
		super(Translator.localize("action.page-setup"),ResourceLoaderWrapper.lookupIcon("action.page-setup"));
	}
	public void actionPerformed(ActionEvent ae) {
		PrintManager.getInstance().showPageSetupDialog();
	}
}



