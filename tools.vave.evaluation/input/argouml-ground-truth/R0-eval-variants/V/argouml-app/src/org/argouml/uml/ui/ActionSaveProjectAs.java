package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.ActionSaveProject;


public class ActionSaveProjectAs extends ActionSaveProject {
	public ActionSaveProjectAs() {
		super(Translator.localize("action.save-project-as"),ResourceLoaderWrapper.lookupIcon("action.save-project-as"));
	}
	public void actionPerformed(ActionEvent e) {
		ProjectBrowser.getInstance().trySave(false,true);
	}
	private static final long serialVersionUID = -1209396991311217989l;
}



