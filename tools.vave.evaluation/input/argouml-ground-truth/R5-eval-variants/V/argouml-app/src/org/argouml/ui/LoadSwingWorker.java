package org.argouml.ui;

import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import org.argouml.i18n.Translator;
import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.ArgoFrame;
import org.argouml.ui.ProjectBrowser;


public class LoadSwingWorker extends SwingWorker {
	private boolean showUi;
	private File file;
	public LoadSwingWorker(File aFile,boolean aShowUi) {
		super("ArgoLoadProjectThread");
		this.showUi = aShowUi;
		this.file = aFile;
	}
	public Object construct(ProgressMonitor pmw) {
		Thread currentThread = Thread.currentThread();
		currentThread.setPriority(currentThread.getPriority() - 1);
		ProjectBrowser.getInstance().loadProject(file,showUi,pmw);
		return null;
	}
	public ProgressMonitor initProgressMonitorWindow() {
		UIManager.put("ProgressMonitor.progressText",Translator.localize("filechooser.open-project"));
		Object[]msgArgs = new Object[] {this.file.getPath()};
		return new ProgressMonitorWindow(ArgoFrame.getInstance(),Translator.messageFormat("dialog.openproject.title",msgArgs));
	}
	public void finished() {
		super.finished();
		try {
			ProjectBrowser.getInstance().addFileSaved(file);
		}catch (IOException exc) {
		}
	}
}



