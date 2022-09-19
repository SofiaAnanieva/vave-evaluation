package org.argouml.uml.ui.behavior.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;


class UMLCollaborationRepresentedClassifierComboBoxModel extends UMLComboBoxModel2 {
	public UMLCollaborationRepresentedClassifierComboBoxModel() {
		super("representedClassifier",true);
	}
	protected void buildModelList() {
		Collection classifiers = new ArrayList();
		Project p = ProjectManager.getManager().getCurrentProject();
		for (Object model:p.getUserDefinedModelList()) {
			Collection c = Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getClassifier());
			for (Object cls:c) {
				Collection s = Model.getModelManagementHelper().getAllSurroundingNamespaces(cls);
				if (!s.contains(getTarget())) {
					classifiers.add(cls);
				}
			}
		}
		setElements(classifiers);
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAClassifier(element)&&Model.getFacade().getRepresentedClassifier(getTarget()) == element;
	}
	protected Object getSelectedModelElement() {
		return Model.getFacade().getRepresentedClassifier(getTarget());
	}
	public void modelChange(UmlChangeEvent evt) {
	}
}



