package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;


public class ActionOpenGoals extends UndoableAction {
	public ActionOpenGoals() {
		super(Translator.localize("action.design-goals"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.design-goals"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		GoalsDialog d = new GoalsDialog();
		d.setVisible(true);
	}
}



