package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.ActionNavigateAction;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionExpressionModel;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;


public class PropPanelArgument extends PropPanelModelElement {
	public PropPanelArgument() {
		super("label.argument",lookupIcon("Argument"));
		addField(Translator.localize("label.name"),getNameTextField());
		UMLExpressionModel2 expressionModel = new UMLExpressionExpressionModel(this,"expression");
		JTextArea ebf = new UMLExpressionBodyField(expressionModel,true);
		ebf.setFont(LookAndFeelMgr.getInstance().getStandardFont());
		ebf.setRows(3);
		addField(Translator.localize("label.value"),new JScrollPane(ebf));
		addField(Translator.localize("label.language"),new UMLExpressionLanguageField(expressionModel,true));
		addAction(new ActionNavigateAction());
		addAction(getDeleteAction());
	}
	private static final long serialVersionUID = 6737211630130267264l;
}



