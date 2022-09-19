package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;
import org.argouml.util.ArgoFrame;


public class ActionRevertToSaved extends AbstractAction {
	public ActionRevertToSaved() {
		super(Translator.localize("action.revert-to-saved"));
	}
	public void actionPerformed(ActionEvent e) {
		Project p = ProjectManager.getManager().getCurrentProject();
		if (p == null||!ProjectBrowser.getInstance().getSaveAction().isEnabled()) {
			return;
		}
		String message = MessageFormat.format(Translator.localize("optionpane.revert-to-saved-confirm"),new Object[] {p.getName()});
		int response = JOptionPane.showConfirmDialog(ArgoFrame.getInstance(),message,Translator.localize("optionpane.revert-to-saved-confirm-title"),JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			ProjectBrowser.getInstance().loadProjectWithProgressMonitor(new File(p.getURI()),true);
		}
	}
}



