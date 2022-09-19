package org.argouml.uml.ui.behavior.common_behavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddContextSignal extends AbstractActionAddModelElement2 {
	public ActionAddContextSignal() {
		super();
	}
	protected List getChoices() {
		List ret = new ArrayList();
		Object model = ProjectManager.getManager().getCurrentProject().getModel();
		if (getTarget() != null) {
			ret.addAll(Model.getModelManagementHelper().getAllBehavioralFeatures(model));
		}
		return ret;
	}
	protected List getSelected() {
		List ret = new ArrayList();
		ret.addAll(Model.getFacade().getContexts(getTarget()));
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-contexts");
	}
	protected void doIt(Collection selected) {
		Model.getCommonBehaviorHelper().setContexts(getTarget(),selected);
	}
}



