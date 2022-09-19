package org.argouml.uml.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.UMLExpressionModel2;


public class UMLExpressionLanguageField extends JTextField implements DocumentListener,UMLUserInterfaceComponent {
	private UMLExpressionModel2 model;
	private boolean notifyModel;
	public UMLExpressionLanguageField(UMLExpressionModel2 expressionModel,boolean notify) {
		model = expressionModel;
		notifyModel = notify;
		getDocument().addDocumentListener(this);
		setToolTipText(Translator.localize("label.language.tooltip"));
		setFont(LookAndFeelMgr.getInstance().getStandardFont());
	}
	public void targetChanged() {
		if (notifyModel) {
			model.targetChanged();
		}
		update();
	}
	public void targetReasserted() {
	}
	private void update() {
		String oldText = getText();
		String newText = model.getLanguage();
		if (oldText == null||newText == null||!oldText.equals(newText)) {
			if (oldText != newText) {
				setText(newText);
			}
		}
	}
	public void changedUpdate(final DocumentEvent p1) {
		model.setLanguage(getText());
	}
	public void removeUpdate(final DocumentEvent p1) {
		model.setLanguage(getText());
	}
	public void insertUpdate(final DocumentEvent p1) {
		model.setLanguage(getText());
	}
}



