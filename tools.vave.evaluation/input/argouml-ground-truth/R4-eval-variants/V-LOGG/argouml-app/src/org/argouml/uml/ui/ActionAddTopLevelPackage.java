package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.tigris.gef.undo.UndoableAction;


public class ActionAddTopLevelPackage extends UndoableAction {
	public ActionAddTopLevelPackage() {
		super(Translator.localize("action.add-top-level-package"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.add-top-level-package"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		Project p = ProjectManager.getManager().getCurrentProject();
		int numPacks = p.getUserDefinedModelList().size();
		String nameStr = "package_" + (numPacks + 1);
		Object model = Model.getModelManagementFactory().createModel();
		Model.getCoreHelper().setName(model,nameStr);
		p.addMember(model);
		super.actionPerformed(ae);
		new ActionClassDiagram().actionPerformed(ae);
	}
}



