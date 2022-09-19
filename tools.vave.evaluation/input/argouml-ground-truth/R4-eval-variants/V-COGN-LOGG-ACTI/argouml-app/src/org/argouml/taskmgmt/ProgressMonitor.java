package org.argouml.taskmgmt;


public interface ProgressMonitor extends ProgressListener {
	void updateProgress(int progress);
	void updateSubTask(String name);
	void updateMainTask(String name);
	boolean isCanceled();
	void setMaximumProgress(int max);
	void notifyNullAction();
	void notifyMessage(String title,String introduction,String message);
	public void close();
}



