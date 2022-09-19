package org.argouml.uml.ui.behavior.activity_graphs;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLObjectFlowStateClassifierComboBoxModel extends UMLComboBoxModel2 {
	public UMLObjectFlowStateClassifierComboBoxModel() {
		super("type",false);
	}
	protected boolean isValidElement(Object o) {
		return Model.getFacade().isAClassifier(o);
	}
	protected void buildModelList() {
		Object model = ProjectManager.getManager().getCurrentProject().getModel();
		Collection newList = new ArrayList(Model.getCoreHelper().getAllClassifiers(model));
		if (getTarget() != null) {
			Object type = Model.getFacade().getType(getTarget());
			if (type != null)if (!newList.contains(type))newList.add(type);
		}
		setElements(newList);
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getType(getTarget());
		}
		return null;
	}
	public void modelChanged(UmlChangeEvent evt) {
		buildingModel = true;
		buildModelList();
		buildingModel = false;
		setSelectedItem(getSelectedModelElement());
	}
}



