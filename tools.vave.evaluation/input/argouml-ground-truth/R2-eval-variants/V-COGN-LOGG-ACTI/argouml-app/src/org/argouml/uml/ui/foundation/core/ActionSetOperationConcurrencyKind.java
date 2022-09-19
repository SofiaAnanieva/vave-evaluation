package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JRadioButton;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;
import org.tigris.gef.undo.UndoableAction;


public class ActionSetOperationConcurrencyKind extends UndoableAction {
	private static final ActionSetOperationConcurrencyKind SINGLETON = new ActionSetOperationConcurrencyKind();
	public static final String SEQUENTIAL_COMMAND = "sequential";
	public static final String GUARDED_COMMAND = "guarded";
	public static final String CONCURRENT_COMMAND = "concurrent";
	protected ActionSetOperationConcurrencyKind() {
		super(Translator.localize("Set"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("Set"));
	}
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource()instanceof JRadioButton) {
			JRadioButton source = (JRadioButton) e.getSource();
			String actionCommand = source.getActionCommand();
			Object target = ((UMLRadioButtonPanel) source.getParent()).getTarget();
			if (Model.getFacade().isAOperation(target)) {
				Object m = target;
				Object kind = null;
				if (actionCommand.equals(SEQUENTIAL_COMMAND)) {
					kind = Model.getConcurrencyKind().getSequential();
				}else if (actionCommand.equals(GUARDED_COMMAND)) {
					kind = Model.getConcurrencyKind().getGuarded();
				}else {
					kind = Model.getConcurrencyKind().getConcurrent();
				}
				Model.getCoreHelper().setConcurrency(m,kind);
			}
		}
	}
	public static ActionSetOperationConcurrencyKind getInstance() {
		return SINGLETON;
	}
}



