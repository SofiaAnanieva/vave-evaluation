package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.FindDialog;
import org.tigris.gef.undo.UndoableAction;


class ActionFind extends UndoableAction {
	private String name;
	public ActionFind() {
		super(Translator.localize("action.find"));
		name = "action.find";
		putValue(Action.SHORT_DESCRIPTION,Translator.localize(name));
		Icon icon = ResourceLoaderWrapper.lookupIcon(name);
		putValue(Action.SMALL_ICON,icon);
	}
	public void actionPerformed(ActionEvent ae) {
		FindDialog.getInstance().setVisible(true);
	}
}



