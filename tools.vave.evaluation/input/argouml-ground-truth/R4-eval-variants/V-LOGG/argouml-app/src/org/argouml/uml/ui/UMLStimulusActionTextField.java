package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.argouml.uml.ui.UMLUserInterfaceContainer;


public class UMLStimulusActionTextField extends JTextField implements DocumentListener,UMLUserInterfaceComponent,PropertyChangeListener {
	private UMLUserInterfaceContainer theContainer;
	private UMLStimulusActionTextProperty theProperty;
	public UMLStimulusActionTextField(UMLUserInterfaceContainer container,UMLStimulusActionTextProperty property) {
		theContainer = container;
		theProperty = property;
		getDocument().addDocumentListener(this);
		update();
	}
	public void targetChanged() {
		theProperty.targetChanged();
		update();
	}
	public void targetReasserted() {
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (theProperty.isAffected(event)) {
			Object eventSource = event.getSource();
			Object target = theContainer.getTarget();
			if (eventSource == null||eventSource == target) {
				update();
			}
		}
	}
	private void update() {
		String oldText = getText();
		String newText = theProperty.getProperty(theContainer);
		if (oldText == null||newText == null||!oldText.equals(newText)) {
			if (oldText != newText) {
				setText(newText);
			}
		}
	}
	public void changedUpdate(final DocumentEvent p1) {
		theProperty.setProperty(theContainer,getText());
	}
	public void removeUpdate(final DocumentEvent p1) {
		theProperty.setProperty(theContainer,getText());
	}
	public void insertUpdate(final DocumentEvent p1) {
		theProperty.setProperty(theContainer,getText());
	}
}



