package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.ui.UndoableAction;
import org.argouml.uml.ui.UMLListCellRenderer2;


public class ActionNewToDoItem extends UndoableAction {
	public ActionNewToDoItem() {
		super(Translator.localize("action.new-todo-item"),ResourceLoaderWrapper.lookupIcon("action.new-todo-item"));
		putValue(Action.SHORT_DESCRIPTION,Translator.localize("action.new-todo-item"));
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		AddToDoItemDialog dialog = new AddToDoItemDialog(new UMLListCellRenderer2(true));
		dialog.setVisible(true);
	}
}



