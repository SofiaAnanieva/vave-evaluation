package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLCheckBox2;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetActionAsynchronous extends UndoableAction {
	private static final ActionSetActionAsynchronous SINGLETON = new ActionSetActionAsynchronous();
	protected ActionSetActionAsynchronous() {
		super(Translator.localize("action.set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof UMLCheckBox2) {
			UMLCheckBox2 source = (UMLCheckBox2) e.getSource();
			Object target = source.getTarget();
			if (Model.getFacade().isAAction(target)) {
				Object m = target;
				Model.getCommonBehaviorHelper().setAsynchronous(m,source.isSelected());
			}
		}
	}
	public static ActionSetActionAsynchronous getInstance() {
		return SINGLETON;
	}
	private static final long serialVersionUID = 1683440096488846000l;
}



