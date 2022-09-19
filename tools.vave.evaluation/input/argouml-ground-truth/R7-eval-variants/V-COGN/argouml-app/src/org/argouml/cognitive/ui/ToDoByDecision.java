package org.argouml.cognitive.ui;

import java.util.List;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByDecision extends ToDoPerspective implements ToDoListListener {
	public ToDoByDecision() {
		super("combobox.todo-perspective-decision");
		addSubTreeModel(new GoListToDecisionsToItems());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Decision dec:Designer.theDesigner().getDecisionModel().getDecisionList()) {
			int nMatchingItems = 0;
			path[1] = dec;
			for (ToDoItem item:items) {
				if (!item.supports(dec)) {
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
			for (ToDoItem item:items) {
				if (!item.supports(dec)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(dec,item);
				children[nMatchingItems] = item;
				nMatchingItems++;
			}
			fireTreeNodesChanged(this,path,childIndices,children);
		}
	}
	public void toDoItemsAdded(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Decision dec:Designer.theDesigner().getDecisionModel().getDecisionList()) {
			int nMatchingItems = 0;
			path[1] = dec;
			for (ToDoItem item:items) {
				if (!item.supports(dec)) {
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
			for (ToDoItem item:items) {
				if (!item.supports(dec)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(dec,item);
				children[nMatchingItems] = item;
				nMatchingItems++;
			}
			fireTreeNodesInserted(this,path,childIndices,children);
		}
	}
	public void toDoItemsRemoved(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (Decision dec:Designer.theDesigner().getDecisionModel().getDecisionList()) {
			boolean anyInDec = false;
			for (ToDoItem item:items) {
				if (item.supports(dec)) {
					anyInDec = true;
				}
			}
			if (!anyInDec) {
				continue;
			}
			path[1] = dec;
			fireTreeStructureChanged(path);
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



