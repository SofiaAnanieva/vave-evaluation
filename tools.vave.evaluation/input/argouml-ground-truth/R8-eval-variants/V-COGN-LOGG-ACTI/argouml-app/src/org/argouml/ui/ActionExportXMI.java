package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.util.ArgoFrame;
import org.argouml.ui.ProjectBrowser;


public final class ActionExportXMI extends AbstractAction {
	public ActionExportXMI() {
		super(Translator.localize("action.export-project-as-xmi"));
	}
	public void actionPerformed(ActionEvent e) {
		PersistenceManager pm = PersistenceManager.getInstance();
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(Translator.localize("action.export-project-as-xmi"));
		chooser.setFileView(ProjectFileView.getInstance());
		chooser.setApproveButtonText(Translator.localize("filechooser.export"));
		chooser.setAcceptAllFileFilterUsed(true);
		pm.setXmiFileChooserFilter(chooser);
		String fn = Configuration.getString(PersistenceManager.KEY_PROJECT_NAME_PATH);
		if (fn.length() > 0) {
			fn = PersistenceManager.getInstance().getBaseName(fn);
			chooser.setSelectedFile(new File(fn));
		}
		int result = chooser.showSaveDialog(ArgoFrame.getInstance());
		if (result == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				String name = theFile.getName();
				Configuration.setString(PersistenceManager.KEY_PROJECT_NAME_PATH,PersistenceManager.getInstance().getBaseName(theFile.getPath()));
				name = pm.fixXmiExtension(name);
				theFile = new File(theFile.getParent(),name);
				ProjectBrowser.getInstance().trySaveWithProgressMonitor(false,theFile);
			}
		}
	}
	private static final long serialVersionUID = -3445739054369264482l;
}



