package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JRadioButton;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetChangeability extends UndoableAction {
	private static final ActionSetChangeability SINGLETON = new ActionSetChangeability();
	@Deprecated public static final String ADDONLY_COMMAND = "addonly";
	public static final String CHANGEABLE_COMMAND = "changeable";
	public static final String FROZEN_COMMAND = "frozen";
	protected ActionSetChangeability() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) e.getSource();
			String actionCommand = source.getActionCommand();
			Object target = ((UMLRadioButtonPanel) source.getParent()).getTarget();
			if (Model.getFacade().isAAssociationEnd(target)||Model.getFacade().isAAttribute(target)) {
				Object m = target;
				if (actionCommand.equals(CHANGEABLE_COMMAND)) {
					Model.getCoreHelper().setReadOnly(m,false);
				}else if (actionCommand.equals(ADDONLY_COMMAND)) {
					Model.getCoreHelper().setChangeability(m,Model.getChangeableKind().getAddOnly());
				}else {
					Model.getCoreHelper().setReadOnly(m,true);
				}
			}
		}
	}
	public static ActionSetChangeability getInstance() {
		return SINGLETON;
	}
}



