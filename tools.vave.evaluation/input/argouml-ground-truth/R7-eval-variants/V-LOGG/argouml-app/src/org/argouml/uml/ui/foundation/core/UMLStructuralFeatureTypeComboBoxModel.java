package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;


public class UMLStructuralFeatureTypeComboBoxModel extends UMLComboBoxModel2 {
	public UMLStructuralFeatureTypeComboBoxModel() {
		super("type",true);
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().isAClass(element)||Model.getFacade().isAInterface(element)||Model.getFacade().isADataType(element);
	}
	@SuppressWarnings("unchecked")protected void buildModelList() {
		Set<Object>elements = new TreeSet<Object>(new PathComparator());
		Project p = ProjectManager.getManager().getCurrentProject();
		if (p == null) {
			return;
		}
		for (Object model:p.getUserDefinedModelList()) {
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getUMLClass()));
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getInterface()));
			elements.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(model,Model.getMetaTypes().getDataType()));
		}
		elements.addAll(p.getProfileConfiguration().findByMetaType(Model.getMetaTypes().getClassifier()));
		setElements(elements);
	}
	@SuppressWarnings("unchecked")@Override protected void buildMinimalModelList() {
		Collection list = new ArrayList(1);
		Object element = getSelectedModelElement();
		if (element != null) {
			list.add(element);
		}
		setElements(list);
		setModelInvalid();
	}
	@Override protected boolean isLazy() {
		return true;
	}
	protected Object getSelectedModelElement() {
		Object o = null;
		if (getTarget() != null) {
			o = Model.getFacade().getType(getTarget());
		}
		return o;
	}
	@Override protected void addOtherModelEventListeners(Object newTarget) {
		super.addOtherModelEventListeners(newTarget);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getNamespace(),"ownedElement");
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getUMLClass(),"name");
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getInterface(),"name");
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getDataType(),"name");
	}
	@Override protected void removeOtherModelEventListeners(Object oldTarget) {
		super.removeOtherModelEventListeners(oldTarget);
		Model.getPump().removeClassModelEventListener(this,Model.getMetaTypes().getNamespace(),"ownedElement");
		Model.getPump().removeClassModelEventListener(this,Model.getMetaTypes().getUMLClass(),"name");
		Model.getPump().removeClassModelEventListener(this,Model.getMetaTypes().getInterface(),"name");
		Model.getPump().removeClassModelEventListener(this,Model.getMetaTypes().getDataType(),"name");
	}
	@Override public void modelChanged(UmlChangeEvent evt) {
		Object newSelection = getSelectedModelElement();
		if (getSelectedItem() != newSelection) {
			buildMinimalModelList();
			setSelectedItem(newSelection);
		}
		if (evt.getSource() != getTarget()) {
			setModelInvalid();
		}
	}
}



