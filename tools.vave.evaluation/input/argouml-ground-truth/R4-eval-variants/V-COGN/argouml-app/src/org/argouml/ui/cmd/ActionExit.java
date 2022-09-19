package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;


public class ActionExit extends AbstractAction implements CommandLineInterface {
	public ActionExit() {
		super(Translator.localize("action.exit"),ResourceLoaderWrapper.lookupIcon("action.exit"));
	}
	public void actionPerformed(ActionEvent ae) {
		ProjectBrowser.getInstance().tryExit();
	}
	public boolean doCommand(String argument) {
		System.exit(0);
		return true;
	}
	private static final long serialVersionUID = -6264722939329644183l;
}



