package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;


public class GoListToTypeToItem extends AbstractGoList {
	public Object getChild(Object parent,int index) {
		if (parent instanceof ToDoList) {
			return KnowledgeTypeNode.getTypeList().get(index);
		}
		if (parent instanceof KnowledgeTypeNode) {
			KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.containsKnowledgeType(ktn.getName())) {
						if (index == 0) {
							return item;
						}
						index--;
					}
				}
			}
		}
		throw new IndexOutOfBoundsException("getChild shouldnt get here " + "GoListToTypeToItem");
	}
	public int getChildCount(Object parent) {
		if (parent instanceof ToDoList) {
			return KnowledgeTypeNode.getTypeList().size();
		}
		if (parent instanceof KnowledgeTypeNode) {
			KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
			int count = 0;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.containsKnowledgeType(ktn.getName())) {
						count++;
					}
				}
			}
			return count;
		}
		return 0;
	}
	public int getIndexOfChild(Object parent,Object child) {
		if (parent instanceof ToDoList) {
			return KnowledgeTypeNode.getTypeList().indexOf(child);
		}
		if (parent instanceof KnowledgeTypeNode) {
			List<ToDoItem>candidates = new ArrayList<ToDoItem>();
			KnowledgeTypeNode ktn = (KnowledgeTypeNode) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.containsKnowledgeType(ktn.getName())) {
						candidates.add(item);
					}
				}
			}
			return candidates.indexOf(child);
		}
		return-1;
	}
	public boolean isLeaf(Object node) {
		if (node instanceof ToDoList) {
			return false;
		}
		if (node instanceof KnowledgeTypeNode) {
			KnowledgeTypeNode ktn = (KnowledgeTypeNode) node;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.containsKnowledgeType(ktn.getName())) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public void valueForPathChanged(TreePath path,Object newValue) {
	}
	public void addTreeModelListener(TreeModelListener l) {
	}
	public void removeTreeModelListener(TreeModelListener l) {
	}
}



