package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;


public class UMLMessageActivatorComboBoxModel extends UMLComboBoxModel2 {
	private Object interaction = null;
	public UMLMessageActivatorComboBoxModel() {
		super("activator",false);
	}
	protected void buildModelList() {
		Object target = getTarget();
		if (Model.getFacade().isAMessage(target)) {
			Object mes = target;
			removeAllElements();
		}
	}
	protected boolean isValidElement(Object m) {
		try {
			return((Model.getFacade().isAMessage(m))&&m != getTarget()&&!Model.getFacade().getPredecessors(getTarget()).contains(m)&&Model.getFacade().getInteraction(m) == Model.getFacade().getInteraction(getTarget()));
		}catch (InvalidElementException e) {
			return false;
		}
	}
	protected Object getSelectedModelElement() {
		if (getTarget() != null) {
			return Model.getFacade().getActivator(getTarget());
		}
		return null;
	}
	public void setTarget(Object target) {
		if (Model.getFacade().isAMessage(getTarget())) {
			if (interaction != null) {
				Model.getPump().removeModelEventListener(this,interaction,"message");
			}
		}
		super.setTarget(target);
		if (Model.getFacade().isAMessage(target)) {
			interaction = Model.getFacade().getInteraction(target);
			if (interaction != null) {
				Model.getPump().addModelEventListener(this,interaction,"message");
			}
		}
	}
}



