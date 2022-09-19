package org.argouml.cognitive.ui;

import java.util.List;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByPriority extends ToDoPerspective implements ToDoListListener {
	public ToDoByPriority() {
		super("combobox.todo-perspective-priority");
		addSubTreeModel(new GoListToPriorityToItem());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (PriorityNode pn:PriorityNode.getPriorityList()) {
			path[1] = pn;
			int nMatchingItems = 0;
			synchronized (items) {
				for (ToDoItem item:items) {
					if (item.getPriority() != pn.getPriority()) {
						continue;
					}
					nMatchingItems++;
				}
			}
			if (nMatchingItems == 0) {
				continue;
			}
			int[]childIndices = new int[nMatchingItems];
			Object[]children = new Object[nMatchingItems];
			nMatchingItems = 0;
			synchronized (items) {
				for (ToDoItem item:items) {
					if (item.getPriority() != pn.getPriority()) {
						continue;
					}
					childIndices[nMatchingItems] = getIndexOfChild(pn,item);
					children[nMatchingItems] = item;
					nMatchingItems++;
				}
			}
			fireTreeNodesChanged(this,path,childIndices,children);
		}
	}
	public void toDoItemsAdded(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (PriorityNode pn:PriorityNode.getPriorityList()) {
			path[1] = pn;
			int nMatchingItems = 0;
			synchronized (items) {
				for (ToDoItem item:items) {
					if (item.getPriority() != pn.getPriority()) {
						continue;
					}
					nMatchingItems++;
				}
			}
			if (nMatchingItems == 0) {
				continue;
			}
			int[]childIndices = new int[nMatchingItems];
			Object[]children = new Object[nMatchingItems];
			nMatchingItems = 0;
			synchronized (items) {
				for (ToDoItem item:items) {
					if (item.getPriority() != pn.getPriority()) {
						continue;
					}
					childIndices[nMatchingItems] = getIndexOfChild(pn,item);
					children[nMatchingItems] = item;
					nMatchingItems++;
				}
			}
			fireTreeNodesInserted(this,path,childIndices,children);
		}
	}
	public void toDoItemsRemoved(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (PriorityNode pn:PriorityNode.getPriorityList()) {
			int nodePriority = pn.getPriority();
			boolean anyInPri = false;
			synchronized (items) {
				for (ToDoItem item:items) {
					int pri = item.getPriority();
					if (pri == nodePriority) {
						anyInPri = true;
					}
				}
			}
			if (!anyInPri) {
				continue;
			}
			path[1] = pn;
			fireTreeStructureChanged(path);
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



