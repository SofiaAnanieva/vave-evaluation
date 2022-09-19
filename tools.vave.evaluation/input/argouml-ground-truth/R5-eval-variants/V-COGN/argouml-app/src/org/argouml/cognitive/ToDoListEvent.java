package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.argouml.cognitive.ToDoItem;


public class ToDoListEvent {
	private final List<ToDoItem>items;
	public ToDoListEvent() {
		items = null;
	}
	public ToDoListEvent(final List<ToDoItem>toDoItems) {
		items = Collections.unmodifiableList(new ArrayList<ToDoItem>(toDoItems));
	}
	public List<ToDoItem>getToDoItemList() {
		return items;
	}
}



