package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.tigris.gef.undo.UndoableAction;
import org.argouml.uml.ui.UMLComboBox2;


public abstract class ActionSetMultiplicity extends UndoableAction {
	protected ActionSetMultiplicity() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		Object source = e.getSource();
		if (source instanceof UMLComboBox2) {
			Object selected = ((UMLComboBox2) source).getSelectedItem();
			Object target = ((UMLComboBox2) source).getTarget();
			if (target != null&&selected != null)setSelectedItem(selected,target);
		}
	}
	public abstract void setSelectedItem(Object item,Object target);
}



