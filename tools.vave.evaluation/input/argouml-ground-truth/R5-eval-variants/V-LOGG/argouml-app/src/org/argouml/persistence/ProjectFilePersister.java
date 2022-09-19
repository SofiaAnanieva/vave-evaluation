package org.argouml.persistence;

import java.io.File;
import org.argouml.kernel.Project;
import org.argouml.taskmgmt.ProgressListener;


public interface ProjectFilePersister {
	void save(Project project,File file)throws SaveException,InterruptedException;
	Project doLoad(File file)throws OpenException,InterruptedException;
	public void addProgressListener(ProgressListener listener);
	public void removeProgressListener(ProgressListener listener);
}



