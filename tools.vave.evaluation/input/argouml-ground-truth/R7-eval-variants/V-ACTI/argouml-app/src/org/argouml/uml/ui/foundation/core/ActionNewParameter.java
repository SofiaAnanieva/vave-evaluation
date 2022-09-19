package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;


public class ActionNewParameter extends AbstractActionNewModelElement {
	public ActionNewParameter() {
		super("button.new-parameter");
		putValue(Action.NAME,Translator.localize("button.new-parameter"));
	}
	public void actionPerformed(ActionEvent e) {
		Object target = TargetManager.getInstance().getModelTarget();
		if (Model.getFacade().isAParameter(target)) {
			target = Model.getFacade().getModelElementContainer(target);
		}
		if (target != null) {
			super.actionPerformed(e);
			Project currentProject = ProjectManager.getManager().getCurrentProject();
			Object paramType = currentProject.getDefaultParameterType();
			TargetManager.getInstance().setTarget(Model.getCoreFactory().buildParameter(target,paramType));
		}
	}
}



