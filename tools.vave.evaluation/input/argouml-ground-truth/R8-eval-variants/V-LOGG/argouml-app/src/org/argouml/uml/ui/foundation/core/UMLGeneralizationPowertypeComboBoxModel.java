package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;


public class UMLGeneralizationPowertypeComboBoxModel extends UMLComboBoxModel2 {
	public UMLGeneralizationPowertypeComboBoxModel() {
		super("powertype",true);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getNamespace(),"ownedElement");
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getPowertype(getTarget());
		}
		return null;
	}
	protected void buildModelList() {
		Set<Object>elements = new TreeSet<Object>(new PathComparator());
		Project p = ProjectManager.getManager().getCurrentProject();
		for (Object model:p.getUserDefinedModelList()) {
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getClassifier()));
		}
		elements.addAll(p.getProfileConfiguration().findByMetaType(Model.getMetaTypes().getClassifier()));
		removeAllElements();
		addAll(elements);
	}
	@Override protected void buildMinimalModelList() {
		Collection list = new ArrayList(1);
		Object element = getSelectedModelElement();
		if (element == null) {
			element = " ";
		}
		list.add(element);
		setElements(list);
		setModelInvalid();
	}
	@Override protected boolean isLazy() {
		return true;
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAClassifier(element);
	}
}



