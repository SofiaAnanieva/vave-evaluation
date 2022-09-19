package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.ActionNavigateTransition;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionExpressionModel;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


public class PropPanelGuard extends PropPanelModelElement {
	private static final long serialVersionUID = 3698249606426850936l;
	public PropPanelGuard() {
		super("label.guard",lookupIcon("Guard"));
		addField("label.name",getNameTextField());
		addField("label.transition",new JScrollPane(getSingleRowScroll(new UMLGuardTransitionListModel())));
		addSeparator();
		JPanel exprPanel = createBorderPanel("label.expression");
		UMLExpressionModel2 expressionModel = new UMLExpressionExpressionModel(this,"expression");
		JTextArea ebf = new UMLExpressionBodyField(expressionModel,true);
		ebf.setFont(LookAndFeelMgr.getInstance().getStandardFont());
		ebf.setRows(1);
		exprPanel.add(new JScrollPane(ebf));
		exprPanel.add(new UMLExpressionLanguageField(expressionModel,true));
		add(exprPanel);
		addAction(new ActionNavigateTransition());
		addAction(new ActionNewStereotype());
		addAction(getDeleteAction());
	}
}



