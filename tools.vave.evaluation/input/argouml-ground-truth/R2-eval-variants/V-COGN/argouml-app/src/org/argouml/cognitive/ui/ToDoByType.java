package org.argouml.cognitive.ui;

import java.util.List;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByType extends ToDoPerspective implements ToDoListListener {
	public ToDoByType() {
		super("combobox.todo-perspective-type");
		addSubTreeModel(new GoListToTypeToItem());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		for (KnowledgeTypeNode ktn:KnowledgeTypeNode.getTypeList()) {
			String kt = ktn.getName();
			path[1] = ktn;
			int nMatchingItems = 0;
			for (ToDoItem item:items) {
				if (!item.containsKnowledgeType(kt)) {
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
				if (!item.containsKnowledgeType(kt)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(ktn,item);
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
		for (KnowledgeTypeNode ktn:KnowledgeTypeNode.getTypeList()) {
			String kt = ktn.getName();
			path[1] = ktn;
			int nMatchingItems = 0;
			for (ToDoItem item:items) {
				if (!item.containsKnowledgeType(kt)) {
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
				if (!item.containsKnowledgeType(kt)) {
					continue;
				}
				childIndices[nMatchingItems] = getIndexOfChild(ktn,item);
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
		for (KnowledgeTypeNode ktn:KnowledgeTypeNode.getTypeList()) {
			boolean anyInKT = false;
			String kt = ktn.getName();
			for (ToDoItem item:items) {
				if (item.containsKnowledgeType(kt)) {
					anyInKT = true;
				}
			}
			if (!anyInKT) {
				continue;
			}
			path[1] = ktn;
			fireTreeStructureChanged(path);
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



