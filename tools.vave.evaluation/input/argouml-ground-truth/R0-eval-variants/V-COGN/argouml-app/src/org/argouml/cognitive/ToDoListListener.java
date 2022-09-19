package org.argouml.cognitive;


public interface ToDoListListener extends java.util.EventListener {
	void toDoListChanged(ToDoListEvent tde);
	void toDoItemsChanged(ToDoListEvent tde);
	void toDoItemsAdded(ToDoListEvent tde);
	void toDoItemsRemoved(ToDoListEvent tde);
}



