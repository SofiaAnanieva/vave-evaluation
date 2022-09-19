package org.argouml.ui;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;


public class TreeModelSupport extends PerspectiveSupport {
	private EventListenerList listenerList = new EventListenerList();
	public TreeModelSupport(String name) {
		super(name);
	}
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class,l);
	}
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class,l);
	}
	protected void fireTreeNodesChanged(final Object source,final Object[]path,final int[]childIndices,final Object[]children) {
		Object[]listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null) {
					e = new TreeModelEvent(source,path,childIndices,children);
				}
				((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
			}
		}
	}
	protected void fireTreeNodesInserted(Object source,Object[]path,int[]childIndices,Object[]children) {
		Object[]listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null) {
					e = new TreeModelEvent(source,path,childIndices,children);
				}
				((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
			}
		}
	}
	protected void fireTreeNodesRemoved(Object source,Object[]path,int[]childIndices,Object[]children) {
		Object[]listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null) {
					e = new TreeModelEvent(source,path,childIndices,children);
				}
				((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
			}
		}
	}
	protected void fireTreeStructureChanged(Object[]path) {
		fireTreeStructureChanged(this,path);
	}
	protected void fireTreeStructureChanged(Object source,Object[]path) {
		fireTreeStructureChanged(source,path,null,null);
	}
	public void fireTreeStructureChanged(Object source,Object[]path,int[]childIndices,Object[]children) {
		Object[]listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners. - 2;i >= 0;i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null) {
					e = new TreeModelEvent(source,path,childIndices,children);
				}
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
			}
		}
	}
}



