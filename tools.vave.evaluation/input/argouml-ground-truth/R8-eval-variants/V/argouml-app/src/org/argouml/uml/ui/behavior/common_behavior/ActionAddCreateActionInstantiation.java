package org.argouml.uml.ui.behavior.common_behavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddCreateActionInstantiation extends AbstractActionAddModelElement2 {
	private Object choiceClass = Model.getMetaTypes().getClassifier();
	public ActionAddCreateActionInstantiation() {
		super();
		setMultiSelect(false);
	}
	protected void doIt(Collection selected) {
		if (selected != null&&selected.size() >= 1) {
			Model.getCommonBehaviorHelper().setInstantiation(getTarget(),selected.iterator().next());
		}else {
			Model.getCommonBehaviorHelper().setInstantiation(getTarget(),null);
		}
	}
	protected List getChoices() {
		List ret = new ArrayList();
		if (getTarget() != null) {
			Project p = ProjectManager.getManager().getCurrentProject();
			Object model = p.getRoot();
			ret.addAll(Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(model,choiceClass));
		}
		return ret;
	}
	protected String getDialogTitle() {
		return Translator.localize("dialog.title.add-instantiation");
	}
	protected List getSelected() {
		List ret = new ArrayList();
		Object instantiation = Model.getCommonBehaviorHelper().getInstantiation(getTarget());
		if (instantiation != null) {
			ret.add(instantiation);
		}
		return ret;
	}
	private static final long serialVersionUID = -7108663482184056359l;
}



