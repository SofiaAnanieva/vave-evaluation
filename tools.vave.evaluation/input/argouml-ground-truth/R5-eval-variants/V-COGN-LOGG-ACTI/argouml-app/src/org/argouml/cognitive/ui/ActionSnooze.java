package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;


public class ActionSnooze extends ToDoItemAction {
	public ActionSnooze() {
		super("action.snooze-critic",true);
	}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		if (!(getRememberedTarget()instanceof ToDoItem))return;
		ToDoItem item = (ToDoItem) getRememberedTarget();
		Poster p = item.getPoster();
		p.snooze();
		TabToDo.incrementNumHushes();
	}
}



