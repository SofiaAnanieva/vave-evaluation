package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddClientDependencyAction extends AbstractActionAddModelElement2 {
	public ActionAddClientDependencyAction() {
		super();
		setMultiSelect(true);
	}
	protected void doIt(Collection selected) {
		Set oldSet = new HashSet(getSelected());
		for (Object client:selected) {
			if (oldSet.contains(client)) {
				oldSet.remove(client);
			}else {
				Model.getCoreFactory().buildDependency(getTarget(),client);
			}
		}
		Collection toBeDeleted = new ArrayList();
		Collection dependencies = Model.getFacade().getClientDependencies(getTarget());
		for (Object dependency:dependencies) {
			if (oldSet.containsAll(Model.getFacade().getSuppliers(dependency))) {
				toBeDeleted.add(dependency);
			}
		}
		ProjectManager.getManager().getCurrentProject().moveToTrash(toBeDeleted);
	}
	protected List getChoices() {
		List ret = new ArrayList();
		Object model = ProjectManager.getManager().getCurrentProject().getModel();
		if (getTarget() != null) {
			ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,"org.omg.uml.foundation.core.ModelElement"));
			ret.remove(getTarget());
		}
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-client-dependency");
	}
	protected List getSelected() {
		List v = new ArrayList();
		Collection c = Model.getFacade().getClientDependencies(getTarget());
		for (Object cd:c) {
			v.addAll(Model.getFacade().getSuppliers(cd));
		}
		return v;
	}
}



