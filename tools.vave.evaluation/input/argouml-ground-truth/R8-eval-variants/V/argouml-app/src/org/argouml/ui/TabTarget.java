package org.argouml.ui;

import org.argouml.ui.targetmanager.TargetListener;


public interface TabTarget extends TargetListener {
	public void setTarget(Object target);
	public Object getTarget();
	public void refresh();
	public boolean shouldBeEnabled(Object target);
}



