package org.argouml.uml.ui.behavior.activity_graphs;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetSynch extends UndoableAction {
	private static final ActionSetSynch SINGLETON = new ActionSetSynch();
	protected ActionSetSynch() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAObjectFlowState(target)) {
				Object m = target;
				Model.getActivityGraphsHelper().setSynch(m,source.isSelected());
			}
		}
	}
	public static ActionSetSynch getInstance() {
		return SINGLETON;
	}
}



