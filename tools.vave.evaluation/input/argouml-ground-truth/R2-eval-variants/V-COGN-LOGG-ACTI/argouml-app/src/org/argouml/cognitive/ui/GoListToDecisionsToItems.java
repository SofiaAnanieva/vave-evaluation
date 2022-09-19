package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;


public class GoListToDecisionsToItems extends AbstractGoList {
	public Object getChild(Object parent,int index) {
		if (parent instanceof ToDoList) {
			return getDecisionList().get(index);
		}
		if (parent instanceof Decision) {
			Decision dec = (Decision) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPoster().supports(dec)) {
						if (index == 0) {
							return item;
						}
						index--;
					}
				}
			}
		}
		throw new IndexOutOfBoundsException("getChild shouldn\'t get here " + "GoListToDecisionsToItems");
	}
	private int getChildCountCond(Object parent,boolean stopafterone) {
		if (parent instanceof ToDoList) {
			return getDecisionList().size();
		}
		if (parent instanceof Decision) {
			Decision dec = (Decision) parent;
			int count = 0;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPoster().supports(dec)) {
						count++;
					}
					if (stopafterone&&count > 0) {
						break;
					}
				}
			}
			return count;
		}
		return 0;
	}
	public int getChildCount(Object parent) {
		return getChildCountCond(parent,false);
	}
	private boolean hasChildren(Object parent) {
		return getChildCountCond(parent,true) > 0;
	}
	public int getIndexOfChild(Object parent,Object child) {
		if (parent instanceof ToDoList) {
			return getDecisionList().indexOf(child);
		}
		if (parent instanceof Decision) {
			List<ToDoItem>candidates = new ArrayList<ToDoItem>();
			Decision dec = (Decision) parent;
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					if (item.getPoster().supports(dec)) {
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
		if (node instanceof Decision&&hasChildren(node)) {
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
	public List<Decision>getDecisionList() {
		return Designer.theDesigner().getDecisionModel().getDecisionList();
	}
}



