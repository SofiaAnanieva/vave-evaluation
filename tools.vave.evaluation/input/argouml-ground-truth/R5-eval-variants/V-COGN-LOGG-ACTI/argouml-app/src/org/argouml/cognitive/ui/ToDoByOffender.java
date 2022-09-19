package org.argouml.cognitive.ui;

import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;


public class ToDoByOffender extends ToDoPerspective implements ToDoListListener {
	private static final Logger LOG = Logger.getLogger(ToDoByOffender.class);
	public ToDoByOffender() {
		super("combobox.todo-perspective-offender");
		addSubTreeModel(new GoListToOffenderToItem());
	}
	public void toDoItemsChanged(ToDoListEvent tde) {
		LOG.debug("toDoItemsChanged");
		List<ToDoItem>items = tde.getToDoItemList();
		Object[]path = new Object[2];
		path[0] = Designer.theDesigner().getToDoList();
		ListSet allOffenders = Designer.theDesigner().getToDoList().getOffenders();
		synchronized (allOffenders) {
			for (Object off:allOffenders) {
				path[1] = off;
				int nMatchingItems = 0;
				synchronized (items) {
					for (ToDoItem item:items) {
						ListSet offenders = item.getOffenders();
						if (!offenders.contains(off)) {
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
						ListSet offenders = item.getOffenders();
						if (!offenders.contains(off)) {
							continue;
						}
						childIndices[nMatchingItems] = getIndexOfChild(off,item);
						children[nMatchingItems] = item;
						nMatchingItems++;
					}
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
		ListSet allOffenders = Designer.theDesigner().getToDoList().getOffenders();
		synchronized (allOffenders) {
			for (Object off:allOffenders) {
				path[1] = off;
				int nMatchingItems = 0;
				synchronized (items) {
					for (ToDoItem item:items) {
						ListSet offenders = item.getOffenders();
						if (!offenders.contains(off)) {
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
						ListSet offenders = item.getOffenders();
						if (!offenders.contains(off)) {
							continue;
						}
						childIndices[nMatchingItems] = getIndexOfChild(off,item);
						children[nMatchingItems] = item;
						nMatchingItems++;
					}
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
		ListSet allOffenders = Designer.theDesigner().getToDoList().getOffenders();
		synchronized (allOffenders) {
			for (Object off:allOffenders) {
				boolean anyInOff = false;
				synchronized (items) {
					for (ToDoItem item:items) {
						ListSet offenders = item.getOffenders();
						if (offenders.contains(off)) {
							anyInOff = true;
							break;
						}
					}
				}
				if (!anyInOff) {
					continue;
				}
				LOG.debug("toDoItemRemoved updating PriorityNode");
				path[1] = off;
				fireTreeStructureChanged(path);
			}
		}
	}
	public void toDoListChanged(ToDoListEvent tde) {
	}
}



