package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;


public class ActionNew extends AbstractAction {
	public ActionNew() {
		super(Translator.localize("action.new"),ResourceLoaderWrapper.lookupIcon("action.new"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.new"));
	}
	public void actionPerformed(ActionEvent e) {
		Model.getPump().flushModelEvents();
		Model.getPump().stopPumpingEvents();
		Model.getPump().flushModelEvents();
		Project p = ProjectManager.getManager().getCurrentProject();
		if (getValue("non-interactive") == null) {
			if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
				return;
			}
		}
		ProjectBrowser.getInstance().clearDialogs();
		TargetManager.getInstance().cleanHistory();
		p.remove();
		p = ProjectManager.getManager().makeEmptyProject();
		TargetManager.getInstance().setTarget(p.getDiagramList().get(0));
		Model.getPump().startPumpingEvents();
	}
	private static final long serialVersionUID = -3943153836514178100l;
}



