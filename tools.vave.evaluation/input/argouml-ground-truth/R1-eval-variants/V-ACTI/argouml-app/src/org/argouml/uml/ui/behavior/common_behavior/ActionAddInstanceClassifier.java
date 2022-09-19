package org.argouml.uml.ui.behavior.common_behavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


public class ActionAddInstanceClassifier extends AbstractActionAddModelElement2 {
	private Object choiceClass = Model.getMetaTypes().getClassifier();
	public ActionAddInstanceClassifier() {
		super();
	}
	public ActionAddInstanceClassifier(Object choice) {
		super();
		choiceClass = choice;
	}
	@Override protected void doIt(Collection selected) {
		Model.getCommonBehaviorHelper().setClassifiers(getTarget(),selected);
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
		return Translator.localize("dialog.title.add-specifications");
	}
	protected List getSelected() {
		List ret = new ArrayList();
		ret.addAll(Model.getFacade().getClassifiers(getTarget()));
		return ret;
	}
}



