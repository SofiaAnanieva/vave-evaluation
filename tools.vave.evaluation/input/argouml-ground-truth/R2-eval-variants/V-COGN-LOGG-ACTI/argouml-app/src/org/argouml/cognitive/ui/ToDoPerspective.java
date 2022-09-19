package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.TreeModelComposite;


public abstract class ToDoPerspective extends TreeModelComposite {
	private static final Logger LOG = Logger.getLogger(ToDoPerspective.class);
	private boolean flat;
	private List<ToDoItem>flatChildren;
	public ToDoPerspective(String name) {
		super(name);
		flatChildren = new ArrayList<ToDoItem>();
	}
	@Override public Object getChild(Object parent,int index) {
		if (flat&&parent == getRoot()) {
			return flatChildren.get(index);
		}
		return super.getChild(parent,index);
	}
	@Override public int getChildCount(Object parent) {
		if (flat&&parent == getRoot()) {
			return flatChildren.size();
		}
		return super.getChildCount(parent);
	}
	@Override public int getIndexOfChild(Object parent,Object child) {
		if (flat&&parent == getRoot()) {
			return flatChildren.indexOf(child);
		}
		return super.getIndexOfChild(parent,child);
	}
	public void setFlat(boolean b) {
		flat = false;
		if (b) {
			calcFlatChildren();
		}
		flat = b;
	}
	public boolean getFlat() {
		return flat;
	}
	public void calcFlatChildren() {
		flatChildren.clear();
		addFlatChildren(getRoot());
	}
	public void addFlatChildren(Object node) {
		if (node == null) {
			return;
		}
		LOG.debug("addFlatChildren");
		if ((node instanceof ToDoItem)&&!flatChildren.contains(node)) {
			flatChildren.add((ToDoItem) node);
		}
		int nKids = getChildCount(node);
		for (int i = 0;i < nKids;i++) {
			addFlatChildren(getChild(node,i));
		}
	}
}



