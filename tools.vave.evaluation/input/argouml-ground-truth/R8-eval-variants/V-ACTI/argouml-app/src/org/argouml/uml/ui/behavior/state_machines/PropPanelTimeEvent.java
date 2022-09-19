package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLTimeExpressionModel;


public class PropPanelTimeEvent extends PropPanelEvent {
	public PropPanelTimeEvent() {
		super("label.time.event",lookupIcon("TimeEvent"));
	}
	@Override public void initialize() {
		super.initialize();
		UMLExpressionModel2 whenModel = new UMLTimeExpressionModel(this,"when");
		JPanel whenPanel = createBorderPanel("label.when");
		whenPanel.add(new JScrollPane(new UMLExpressionBodyField(whenModel,true)));
		whenPanel.add(new UMLExpressionLanguageField(whenModel,false));
		add(whenPanel);
		addAction(getDeleteAction());
	}
}



