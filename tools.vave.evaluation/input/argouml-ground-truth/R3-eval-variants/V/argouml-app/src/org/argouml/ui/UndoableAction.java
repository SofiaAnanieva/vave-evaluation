package org.argouml.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;


public abstract class UndoableAction extends AbstractAction {
	public UndoableAction() {
		super();
	}
	public UndoableAction(String name) {
		super(name);
	}
	public UndoableAction(String name,Icon icon) {
		super(name,icon);
	}
	public void actionPerformed(ActionEvent e) {
		final Project p = ProjectManager.getManager().getCurrentProject();
		p.getUndoManager().startInteraction((String) getValue(AbstractAction.NAME));
	}
}



