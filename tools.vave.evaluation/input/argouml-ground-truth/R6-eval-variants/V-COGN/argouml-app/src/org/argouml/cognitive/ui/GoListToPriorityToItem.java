package org.argouml.cognitive.ui;

import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;


public class GoListToPriorityToItem extends AbstractGoList {
	public Object getChild(Object parent,int index) {
		if (parent instanceof ToDoList) {
			return PriorityNode.getPriorityList().get(index);
		}
		if (parent instanceof PriorityNode) {
			PriorityNode pn = (PriorityNode) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPriority() == pn.getPriority()) {
						if (index == 0) {
							return item;
						}
						index--;
					}
				}
			}
		}
		throw new IndexOutOfBoundsException("getChild shouldnt get here " + "GoListToPriorityToItem");
	}
	public int getChildCount(Object parent) {
		if (parent instanceof ToDoList) {
			return PriorityNode.getPriorityList().size();
		}
		if (parent instanceof PriorityNode) {
			PriorityNode pn = (PriorityNode) parent;
			int count = 0;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPriority() == pn.getPriority()) {
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
			return PriorityNode.getPriorityList().indexOf(child);
		}
		if (parent instanceof PriorityNode) {
			int index = 0;
			PriorityNode pn = (PriorityNode) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPriority() == pn.getPriority()) {
						if (item == child) {
							return index;
						}
						index++;
					}
				}
			}
		}
		return-1;
	}
	public boolean isLeaf(Object node) {
		if (node instanceof ToDoList) {
			return false;
		}
		if (node instanceof PriorityNode&&getChildCount(node) > 0) {
			return false;
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



