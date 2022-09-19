package org.argouml.taskmgmt;

import java.util.EventListener;


public interface ProgressListener extends EventListener {
	void progress(ProgressEvent event)throws InterruptedException;
}



