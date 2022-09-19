package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;


public class GoListToPosterToItem extends AbstractGoList {
	public Object getChild(Object parent,int index) {
		return getChildrenList(parent).get(index);
	}
	public int getChildCount(Object parent) {
		return getChildrenList(parent).size();
	}
	public int getIndexOfChild(Object parent,Object child) {
		return getChildrenList(parent).indexOf(child);
	}
	public boolean isLeaf(Object node) {
		if (node instanceof ToDoList) {
			return false;
		}
		if (getChildCount(node) > 0) {
			return false;
		}
		return true;
	}
	public List getChildrenList(Object parent) {
		ListSet allPosters = Designer.theDesigner().getToDoList().getPosters();
		if (parent instanceof ToDoList) {
			return allPosters;
		}
		if (allPosters.contains(parent)) {
			List<ToDoItem>result = new ArrayList<ToDoItem>();
			List<ToDoItem>itemList = Designer.theDesigner().getToDoList().getToDoItemList();
			synchronized (itemList) {
				for (ToDoItem item:itemList) {
					Poster post = item.getPoster();
					if (post == parent) {
						result.add(item);
					}
				}
			}
			return result;
		}
		return Collections.emptyList();
	}
	public void valueForPathChanged(TreePath path,Object newValue) {
	}
	public void addTreeModelListener(TreeModelListener l) {
	}
	public void removeTreeModelListener(TreeModelListener l) {
	}
}



