package org.argouml.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;


public class ActionUndo extends AbstractAction {
	private static final long serialVersionUID = 6544646406482242086l;
	public ActionUndo(String name) {
		super(name);
	}
	public ActionUndo(String name,Icon icon) {
		super(name,icon);
	}
	public void actionPerformed(ActionEvent e) {
		Project p = ProjectManager.getManager().getCurrentProject();
		p.getUndoManager().undo();
	}
}



