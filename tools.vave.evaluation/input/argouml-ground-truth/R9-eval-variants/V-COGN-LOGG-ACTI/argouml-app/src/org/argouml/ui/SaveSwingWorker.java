package org.argouml.ui;

import java.io.File;
import org.argouml.taskmgmt.ProgressMonitor;


public class SaveSwingWorker extends SwingWorker {
	private boolean overwrite;
	private File file;
	private boolean result;
	public SaveSwingWorker(boolean aOverwrite,File aFile) {
		super("ArgoSaveProjectThread");
	}
	public Object construct(ProgressMonitor pmw) {
		return null;
	}
	public ProgressMonitor initProgressMonitorWindow() {
		return null;
	}
	public void finished() {
	}
}



