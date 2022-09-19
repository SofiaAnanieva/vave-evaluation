package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.argouml.uml.ui.UMLExpressionModel2;


public class UMLExpressionBodyField extends JTextArea implements DocumentListener,UMLUserInterfaceComponent,PropertyChangeListener,TargettableModelView {
	private UMLExpressionModel2 model;
	private boolean notifyModel;
	public UMLExpressionBodyField(UMLExpressionModel2 expressionModel,boolean notify) {
		model = expressionModel;
		notifyModel = notify;
		getDocument().addDocumentListener(this);
		setToolTipText(Translator.localize("label.body.tooltip"));
		setFont(LookAndFeelMgr.getInstance().getStandardFont());
		setRows(2);
	}
	public void targetChanged() {
		if (notifyModel) {
			model.targetChanged();
		}
		update();
	}
	public void targetReasserted() {
	}
	public void propertyChange(PropertyChangeEvent event) {
		update();
	}
	private void update() {
		String oldText = getText();
		String newText = model.getBody();
		if (oldText == null||newText == null||!oldText.equals(newText)) {
			if (oldText != newText) {
				setText(newText);
			}
		}
	}
	public void changedUpdate(final DocumentEvent p1) {
		model.setBody(getText());
	}
	public void removeUpdate(final DocumentEvent p1) {
		model.setBody(getText());
	}
	public void insertUpdate(final DocumentEvent p1) {
		model.setBody(getText());
	}
	public TargetListener getTargettableModel() {
		return model;
	}
}



