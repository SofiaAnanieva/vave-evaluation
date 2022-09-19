package org.argouml.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;


public class ActionRedo extends AbstractAction {
	private static final long serialVersionUID = 3921952827170089931l;
	public ActionRedo(String name) {
		super(name);
	}
	public ActionRedo(String name,Icon icon) {
		super(name,icon);
	}
	public void actionPerformed(ActionEvent e) {
		final Project p = ProjectManager.getManager().getCurrentProject();
		p.getUndoManager().redo();
	}
}



