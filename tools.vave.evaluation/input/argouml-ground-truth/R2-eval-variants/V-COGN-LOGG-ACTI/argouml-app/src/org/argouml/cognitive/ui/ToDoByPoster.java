package org.argouml.cognitive.ui;

import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByPoster extends ToDoPerspective implements ToDoListListener {
	private static final Logger LOG = Logger.getLogger(ToDoByPoster.class);
	public ToDoByPoster() {
		super("combobox.todo-perspective-poster");
		addSubTreeModel(new GoListToPosterToItem());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		LOG.debug("toDoItemsChanged");
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		ListSet<Poster>allPosters = Designer.theDesigner().getToDoList().getPosters();
		synchronized (allPosters) {
			for (Poster p:allPosters) {
				path[1] = p;
				int nMatchingItems = 0;
				for (ToDoItem item:items) {
					Poster post = item.getPoster();
					if (post != p) {
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
					Poster post = item.getPoster();
					if (post != p) {
						continue;
					}
					childIndices[nMatchingItems] = getIndexOfChild(p,item);
					children[nMatchingItems] = item;
					nMatchingItems++;
				}
				fireTreeNodesChanged(this,path,childIndices,children);
			}
		}
	}
	public void toDoItemsAdded(ToDoListEvent tde) {
		LOG.debug("toDoItemAdded");
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		ListSet<Poster>allPosters = Designer.theDesigner().getToDoList().getPosters();
		synchronized (allPosters) {
			for (Poster p:allPosters) {
				path[1] = p;
				int nMatchingItems = 0;
				for (ToDoItem item:items) {
					Poster post = item.getPoster();
					if (post != p) {
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
					Poster post = item.getPoster();
					if (post != p) {
						continue;
					}
					childIndices[nMatchingItems] = getIndexOfChild(p,item);
					children[nMatchingItems] = item;
					nMatchingItems++;
				}
				fireTreeNodesInserted(this,path,childIndices,children);
			}
		}
	}
	public void toDoItemsRemoved(ToDoListEvent tde) {
		LOG.debug("toDoItemRemoved");
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		ListSet<Poster>allPosters = Designer.theDesigner().getToDoList().getPosters();
		synchronized (allPosters) {
			for (Poster p:allPosters) {
				boolean anyInPoster = false;
				for (ToDoItem item:items) {
					Poster post = item.getPoster();
					if (post == p) {
						anyInPoster = true;
						break;
					}
				}
				if (!anyInPoster) {
					continue;
				}
				path[1] = p;
				fireTreeStructureChanged(path);
			}
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



