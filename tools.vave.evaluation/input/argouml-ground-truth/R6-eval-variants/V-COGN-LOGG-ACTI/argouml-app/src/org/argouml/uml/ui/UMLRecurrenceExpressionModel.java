package org.argouml.uml.ui;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLRecurrenceExpressionModel extends UMLExpressionModel2 {
	private static final Logger LOG = Logger.getLogger(UMLRecurrenceExpressionModel.class);
	public UMLRecurrenceExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		return Model.getFacade().getRecurrence(TargetManager.getInstance().getTarget());
	}
	public void setExpression(Object expression) {
		Object target = TargetManager.getInstance().getTarget();
		if (target == null) {
			throw new IllegalStateException("There is no target for " + getContainer());
		}
		Model.getCommonBehaviorHelper().setRecurrence(target,expression);
	}
	public Object newExpression() {
		LOG.debug("new boolean expression");
		return Model.getDataTypesFactory().createIterationExpression("","");
	}
}



