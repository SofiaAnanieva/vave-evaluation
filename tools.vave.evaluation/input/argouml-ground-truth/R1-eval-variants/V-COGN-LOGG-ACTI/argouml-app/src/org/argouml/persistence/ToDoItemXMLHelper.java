package org.argouml.persistence;

import org.argouml.cognitive.ToDoItem;
import org.argouml.persistence.TodoTokenTable;


public class ToDoItemXMLHelper {
	private final ToDoItem item;
	public ToDoItemXMLHelper(ToDoItem todoItem) {
		if (todoItem == null)throw new NullPointerException();
		item = todoItem;
	}
	public String getHeadline() {
		return TodoParser.encode(item.getHeadline());
	}
	public String getPriority() {
		String s = TodoTokenTable.STRING_PRIO_HIGH;
		switch (item.getPriority()) {case ToDoItem.HIGH_PRIORITY:
			s = TodoTokenTable.STRING_PRIO_HIGH;
			break;
		case ToDoItem.MED_PRIORITY:
			s = TodoTokenTable.STRING_PRIO_MED;
			break;
		case ToDoItem.LOW_PRIORITY:
			s = TodoTokenTable.STRING_PRIO_LOW;
			break;
		}
		return TodoParser.encode(s);
	}
	public String getMoreInfoURL() {
		return TodoParser.encode(item.getMoreInfoURL());
	}
	public String getDescription() {
		return TodoParser.encode(item.getDescription());
	}
}



