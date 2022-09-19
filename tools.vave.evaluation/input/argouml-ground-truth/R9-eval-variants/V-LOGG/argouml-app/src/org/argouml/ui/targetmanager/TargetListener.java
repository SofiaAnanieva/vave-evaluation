package org.argouml.ui.targetmanager;

import java.util.EventListener;


public interface TargetListener extends EventListener {
	public void targetSet(TargetEvent e);
	public void targetRemoved(TargetEvent e);
	public void targetAdded(TargetEvent e);
}



