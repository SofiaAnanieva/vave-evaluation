package org.argouml.cognitive.ui;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByGoal extends ToDoPerspective implements ToDoListListener {
	public ToDoByGoal() {
		super("combobox.todo-perspective-goal");
		addSubTreeModel(new GoListToGoalsToItems());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Goal g:Designer.theDesigner().getGoalList()) {
			path[1] = g;
			int nMatchingItems = 0;
			for (ToDoItem item:tde.getToDoItemList()) {
				if (!item.supports(g)) {
					continue;
				}
				nMatchingItems++;
			}
			if (nMatchingItems == 0)continue;
			int[]childIndices = new int[nMatchingItems];
			Object[]children = new Object[nMatchingItems];
			nMatchingItems = 0;
			for (ToDoItem item:tde.getToDoItemList()) {
				if (!item.supports(g)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(g,item);
				children[nMatchingItems] = item;
				nMatchingItems++;
			}
			fireTreeNodesChanged(this,path,childIndices,children);
		}
	}
	public void toDoItemsAdded(ToDoListEvent tde) {
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Goal g:Designer.theDesigner().getGoalList()) {
			path[1] = g;
			int nMatchingItems = 0;
			for (ToDoItem item:tde.getToDoItemList()) {
				if (!item.supports(g)) {
					continue;
				}
				nMatchingItems++;
			}
			if (nMatchingItems == 0) {
				continue;
			}
			int[]childIndices = new int[nMatchingItems];
			Object[]children = new Object[nMatchingItems];
			nMatchingItems = 0;
			for (ToDoItem item:tde.getToDoItemList()) {
				if (!item.supports(g)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(g,item);
				children[nMatchingItems] = item;
				nMatchingItems++;
			}
			fireTreeNodesInserted(this,path,childIndices,children);
		}
	}
	public void toDoItemsRemoved(ToDoListEvent tde) {
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Goal g:Designer.theDesigner().getGoalList()) {
			boolean anyInGoal = false;
			for (ToDoItem item:tde.getToDoItemList()) {
				if (item.supports(g))anyInGoal = true;
			}
			if (!anyInGoal)continue;
			path[1] = g;
			fireTreeStructureChanged(path);
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



