package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


@Deprecated public class ActionSetCompositeStateConcurrent extends UndoableAction {
	private static final ActionSetCompositeStateConcurrent SINGLETON = new ActionSetCompositeStateConcurrent();
	protected ActionSetCompositeStateConcurrent() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isACompositeState(target)) {
				Object compositeState = target;
				Model.getStateMachinesHelper().setConcurrent(compositeState,!Model.getFacade().isConcurrent(compositeState));
			}
		}
	}
	public static ActionSetCompositeStateConcurrent getInstance() {
		return SINGLETON;
	}
}



