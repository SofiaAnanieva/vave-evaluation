package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


class UMLChangeExpressionModel extends UMLExpressionModel2 {
	public UMLChangeExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		return Model.getFacade().getChangeExpression(TargetManager.getInstance().getTarget());
	}
	public void setExpression(Object expression) {
		Object target = TargetManager.getInstance().getTarget();
		if (target == null) {
			throw new IllegalStateException("There is no target for " + getContainer());
		}
		Model.getStateMachinesHelper().setChangeExpression(target,expression);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createBooleanExpression("","");
	}
}



