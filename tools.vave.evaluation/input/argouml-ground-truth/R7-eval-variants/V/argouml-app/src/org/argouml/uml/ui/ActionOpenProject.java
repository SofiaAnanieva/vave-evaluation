package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFileView;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.UndoableAction;
import org.argouml.util.ArgoFrame;


public class ActionOpenProject extends UndoableAction implements CommandLineInterface {
	public ActionOpenProject() {
		super(Translator.localize("action.open-project"),ResourceLoaderWrapper.lookupIcon("action.open-project"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.open-project"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Project p = ProjectManager.getManager().getCurrentProject();
		PersistenceManager pm = PersistenceManager.getInstance();
		if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
			return;
		}
		JFileChooser chooser = null;
		if (p != null&&p.getURI() != null) {
			File file = new File(p.getURI());
			if (file.getParentFile() != null) {
				chooser = new JFileChooser(file.getParent());
			}
		}else {
			chooser = new JFileChooser();
		}
		if (chooser == null) {
			chooser = new JFileChooser();
		}
		chooser.setDialogTitle(Translator.localize("filechooser.open-project"));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileView(ProjectFileView.getInstance());
		pm.setOpenFileChooserFilter(chooser);
		String fn = Configuration.getString(PersistenceManager.KEY_OPEN_PROJECT_PATH);
		if (fn.length() > 0) {
			chooser.setSelectedFile(new File(fn));
		}
		int retval = chooser.showOpenDialog(ArgoFrame.getInstance());
		if (retval == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (!theFile.canRead()) {
				FileFilter ffilter = chooser.getFileFilter();
				if (ffilter instanceof AbstractFilePersister) {
					AbstractFilePersister afp = (AbstractFilePersister) ffilter;
					File m = new File(theFile.getPath() + "." + afp.getExtension());
					if (m.canRead()) {
						theFile = m;
					}
				}
				if (!theFile.canRead()) {
					File n = new File(theFile.getPath() + "." + pm.getDefaultExtension());
					if (n.canRead()) {
						theFile = n;
					}
				}
			}
			if (theFile != null) {
				Configuration.setString(PersistenceManager.KEY_OPEN_PROJECT_PATH,theFile.getPath());
				ProjectBrowser.getInstance().loadProjectWithProgressMonitor(theFile,true);
			}
		}
	}
	public boolean doCommand(String argument) {
		return ProjectBrowser.getInstance().loadProject(new File(argument),false,null);
	}
}



