package org.argouml.uml.ui;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLScriptExpressionModel extends UMLExpressionModel2 {
	public UMLScriptExpressionModel(UMLUserInterfaceContainer container,String propertyName) {
		super(container,propertyName);
	}
	public Object getExpression() {
		return Model.getFacade().getScript(TargetManager.getInstance().getTarget());
	}
	public void setExpression(Object expression) {
		Model.getCommonBehaviorHelper().setScript(TargetManager.getInstance().getTarget(),expression);
	}
	public Object newExpression() {
		return Model.getDataTypesFactory().createActionExpression("","");
	}
}



