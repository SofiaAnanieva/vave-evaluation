package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.argouml.ui.ProjectBrowser;


public class ActionReopenProject extends AbstractAction {
	private String filename;
	public ActionReopenProject(String theFilename) {
		super("action.reopen-project");
		filename = theFilename;
	}
	public String getFilename() {
		return filename;
	}
	public void actionPerformed(ActionEvent e) {
		if (!ProjectBrowser.getInstance().askConfirmationAndSave())return;
		File toOpen = new File(filename);
		ProjectBrowser.getInstance().loadProjectWithProgressMonitor(toOpen,true);
	}
}



