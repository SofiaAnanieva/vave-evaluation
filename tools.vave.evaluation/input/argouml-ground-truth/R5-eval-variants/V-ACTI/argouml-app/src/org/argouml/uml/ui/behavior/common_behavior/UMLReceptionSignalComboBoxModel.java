package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Collection;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLReceptionSignalComboBoxModel extends UMLComboBoxModel2 {
	public UMLReceptionSignalComboBoxModel() {
		super("signal",false);
		Model.getPump().addClassModelEventListener(this,Model.getMetaTypes().getNamespace(),"ownedElement");
	}
	protected void buildModelList() {
		Object target = getTarget();
		if (Model.getFacade().isAReception(target)) {
			Object rec = target;
			removeAllElements();
			Project p = ProjectManager.getManager().getCurrentProject();
			Object model = p.getRoot();
			setElements(Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(model,Model.getMetaTypes().getSignal()));
			setSelectedItem(Model.getFacade().getSignal(rec));
		}
	}
	protected boolean isValidElement(Object m) {
		return Model.getFacade().isASignal(m);
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getSignal(getTarget());
		}
		return null;
	}
	public void modelChanged(UmlChangeEvent evt) {
		if (evt instanceof RemoveAssociationEvent) {
			if ("ownedElement".equals(evt.getPropertyName())) {
				Object o = getChangedElement(evt);
				if (contains(o)) {
					buildingModel = true;
					if (o instanceof Collection) {
						removeAll((Collection) o);
					}else {
						removeElement(o);
					}
					buildingModel = false;
				}
			}
		}else {
			super.propertyChange(evt);
		}
	}
}



