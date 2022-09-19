package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.argouml.uml.ui.UMLExpressionModel2;


public class UMLExpressionExpressionModel extends UMLExpressionModel2 {
	public UMLExpressionExpressionModel(UMLUserInterfaceContainer c,String name) {
		super(c,name);
	}
	public Object getExpression() {
		return Model.getFacade().getExpression(TargetManager.getInstance().getTarget());
	}
	public void setExpression(Object expr) {
		Model.getStateMachinesHelper().setExpression(TargetManager.getInstance().getTarget(),expr);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createBooleanExpression("","");
	}
}



