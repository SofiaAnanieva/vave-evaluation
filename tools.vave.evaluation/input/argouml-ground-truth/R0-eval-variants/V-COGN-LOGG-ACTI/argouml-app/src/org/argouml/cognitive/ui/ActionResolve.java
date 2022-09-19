package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;


public class ActionResolve extends ToDoItemAction {
	public ActionResolve() {
		super("action.resolve-item",true);
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		DismissToDoItemDialog dialog = new DismissToDoItemDialog();
		dialog.setTarget(getRememberedTarget());
		dialog.setVisible(true);
	}
}



