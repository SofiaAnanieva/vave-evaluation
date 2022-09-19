package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.HelpBox;


public class ActionHelp extends AbstractAction {
	public ActionHelp() {
		super(Translator.localize("action.help"),ResourceLoaderWrapper.lookupIcon("action.help"));
	}
	public void actionPerformed(ActionEvent ae) {
		HelpBox box = new HelpBox(Translator.localize("action.help"));
		box.setVisible(true);
	}
	private static final long serialVersionUID = 0l;
}



