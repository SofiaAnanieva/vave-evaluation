package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;


public class ActionOpenDecisions extends UndoableAction {
	public ActionOpenDecisions() {
		super(Translator.localize("action.design-issues"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.design-issues"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		DesignIssuesDialog d = new DesignIssuesDialog();
		d.setVisible(true);
	}
}



