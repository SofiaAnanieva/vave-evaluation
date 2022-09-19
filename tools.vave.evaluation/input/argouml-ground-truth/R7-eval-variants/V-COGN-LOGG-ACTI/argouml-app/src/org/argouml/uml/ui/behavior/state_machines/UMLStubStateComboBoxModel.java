package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;
import java.util.ArrayList;
import java.util.Iterator;


public class UMLStubStateComboBoxModel extends UMLComboBoxModel2 {
	public UMLStubStateComboBoxModel() {
		super("stubstate",true);
	}
	protected boolean isValidElement(Object element) {
		return(Model.getFacade().isAStateVertex(element)&&!Model.getFacade().isAConcurrentRegion(element)&&Model.getFacade().getName(element) != null);
	}
	protected void buildModelList() {
		removeAllElements();
		Object stateMachine = null;
		if (Model.getFacade().isASubmachineState(Model.getFacade().getContainer(getTarget()))) {
			stateMachine = Model.getFacade().getSubmachine(Model.getFacade().getContainer(getTarget()));
		}
		if (stateMachine != null) {
			ArrayList v = (ArrayList) Model.getStateMachinesHelper().getAllPossibleSubvertices(Model.getFacade().getTop(stateMachine));
			ArrayList v2 = (ArrayList) v.clone();
			Iterator it = v2.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (!isValidElement(o)) {
					v.remove(o);
				}
			}
			setElements(v);
		}
	}
	protected Object getSelectedModelElement() {
		String objectName = null;
		Object container = null;
		if (getTarget() != null) {
			objectName = Model.getFacade().getReferenceState(getTarget());
			container = Model.getFacade().getContainer(getTarget());
			if (container != null&&Model.getFacade().isASubmachineState(container)&&Model.getFacade().getSubmachine(container) != null) {
				return Model.getStateMachinesHelper().getStatebyName(objectName,Model.getFacade().getTop(Model.getFacade().getSubmachine(container)));
			}
		}
		return null;
	}
}



