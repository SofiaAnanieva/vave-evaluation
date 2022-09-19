package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;


public class ActionRemoveModelElement extends AbstractActionRemoveElement {
	public static final ActionRemoveModelElement SINGLETON = new ActionRemoveModelElement();
	protected ActionRemoveModelElement() {
		super();
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Project p = ProjectManager.getManager().getCurrentProject();
		if (getObjectToRemove() != null&&ActionDeleteModelElements.sureRemove(getObjectToRemove()))p.moveToTrash(getObjectToRemove());
		setObjectToRemove(null);
	}
	public boolean isEnabled() {
		return getObjectToRemove() != null;
	}
}



