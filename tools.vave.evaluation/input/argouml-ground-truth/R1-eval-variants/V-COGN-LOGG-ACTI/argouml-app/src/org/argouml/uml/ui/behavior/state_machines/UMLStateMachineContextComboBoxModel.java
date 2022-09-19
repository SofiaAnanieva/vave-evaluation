package org.argouml.uml.ui.behavior.state_machines;

import java.util.ArrayList;
import java.util.Collection;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLStateMachineContextComboBoxModel extends UMLComboBoxModel2 {
	public UMLStateMachineContextComboBoxModel() {
		super("context",false);
	}
	protected void buildModelList() {
		Collection elements = new ArrayList();
		Project p = ProjectManager.getManager().getCurrentProject();
		for (Object model:p.getUserDefinedModelList()) {
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getClassifier()));
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getBehavioralFeature()));
		}
		setElements(elements);
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAClassifier(element)||Model.getFacade().isABehavioralFeature(element);
	}
	protected Object getSelectedModelElement() {
		return Model.getFacade().getContext(getTarget());
	}
	public void modelChanged(UmlChangeEvent evt) {
	}
}



