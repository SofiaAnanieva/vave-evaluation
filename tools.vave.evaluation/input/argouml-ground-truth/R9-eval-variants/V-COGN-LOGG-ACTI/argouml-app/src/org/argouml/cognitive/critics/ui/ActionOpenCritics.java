package org.argouml.cognitive.critics.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;


public class ActionOpenCritics extends UndoableAction {
	public ActionOpenCritics() {
		super(Translator.localize("action.browse-critics"),null);
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.browse-critics"));
	}
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		CriticBrowserDialog dialog = new CriticBrowserDialog();
		dialog.setVisible(true);
	}
}



