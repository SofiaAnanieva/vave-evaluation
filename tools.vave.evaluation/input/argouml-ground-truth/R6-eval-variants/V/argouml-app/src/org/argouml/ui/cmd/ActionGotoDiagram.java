package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.application.api.CommandLineInterface;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.GotoDialog;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.undo.UndoableAction;


public class ActionGotoDiagram extends UndoableAction implements CommandLineInterface {
	public ActionGotoDiagram() {
		super(Translator.localize("action.goto-diagram"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.goto-diagram"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		new GotoDialog().setVisible(true);
	}
	public boolean doCommand(String argument) {
		Project p = ProjectManager.getManager().getCurrentProject();
		ArgoDiagram d = p.getDiagram(argument);
		if (d != null) {
			TargetManager.getInstance().setTarget(d);
			return true;
		}
		return false;
	}
}



