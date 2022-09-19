package org.argouml.uml.diagram.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.FigText;


public class PropPanelString extends AbstractArgoJPanel implements TabModelTarget,PropertyChangeListener,DocumentListener {
	private FigText target;
	private JLabel nameLabel = new JLabel(Translator.localize("label.text"));
	private JTextField nameField = new JTextField();
	public PropPanelString() {
		super(Translator.localize("tab.string"));
		GridBagLayout gb = new GridBagLayout();
		setLayout(gb);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.ipadx = 3;
		c.ipady = 3;
		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 0;
		gb.setConstraints(nameLabel,c);
		add(nameLabel);
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridy = 0;
		gb.setConstraints(nameField,c);
		add(nameField);
		nameField.getDocument().addDocumentListener(this);
		nameField.setEditable(true);
	}
	public void setTarget(Object t) {
		if (target != null) {
			target.removePropertyChangeListener(this);
		}
		if (t instanceof FigText) {
			target = (FigText) t;
			target.removePropertyChangeListener(this);
			if (isVisible()) {
				target.addPropertyChangeListener(this);
			}
		}
	}
	public Object getTarget() {
		return target;
	}
	public void refresh() {
		setTarget(target);
	}
	public boolean shouldBeEnabled(Object theTarget) {
		return false;
	}
	protected void setTargetName() {
	}
	public void insertUpdate(DocumentEvent e) {
		if (e.getDocument() == nameField.getDocument()&&target != null) {
			target.setText(nameField.getText());
			target.damage();
		}
	}
	public void removeUpdate(DocumentEvent e) {
		insertUpdate(e);
	}
	public void changedUpdate(DocumentEvent e) {
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("editing")&&evt.getNewValue().equals(Boolean.FALSE)) {
			nameField.setText(target.getText());
		}
	}
	public void targetAdded(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetRemoved(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
	public void targetSet(TargetEvent e) {
		setTarget(e.getNewTarget());
	}
}



