package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;


public class PropPanelChangeEvent extends PropPanelEvent {
	public PropPanelChangeEvent() {
		super("label.change.event",lookupIcon("ChangeEvent"));
	}
	@Override public void initialize() {
		super.initialize();
		UMLExpressionModel2 changeModel = new UMLChangeExpressionModel(this,"changeExpression");
		JPanel changePanel = createBorderPanel("label.change");
		changePanel.add(new JScrollPane(new UMLExpressionBodyField(changeModel,true)));
		changePanel.add(new UMLExpressionLanguageField(changeModel,false));
		add(changePanel);
		addAction(getDeleteAction());
	}
}



