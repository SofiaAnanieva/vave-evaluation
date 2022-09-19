package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddAssociationSpecification extends AbstractActionAddModelElement2 {
	private static final ActionAddAssociationSpecification SINGLETON = new ActionAddAssociationSpecification();
	protected ActionAddAssociationSpecification() {
		super();
	}
	protected void doIt(Collection selected) {
		Model.getCoreHelper().setSpecifications(getTarget(),selected);
	}
	protected List getChoices() {
		List ret = new ArrayList();
		if (getTarget() != null) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Object model = p.getRoot();
			ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(model,Model.getMetaTypes().getClassifier()));
		}
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-specifications");
	}
	protected List getSelected() {
		List ret = new ArrayList();
		ret.addAll(Model.getFacade().getSpecifications(getTarget()));
		return ret;
	}
	public static ActionAddAssociationSpecification getInstance() {
		return SINGLETON;
	}
}



