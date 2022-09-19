package org.argouml.ui.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.tigris.gef.base.Diagram;


public class ExplorerTreeNode extends DefaultMutableTreeNode implements PropertyChangeListener {
	private static final long serialVersionUID = -6766504350537675845l;
	private ExplorerTreeModel model;
	private boolean expanded;
	private boolean pending;
	private Set modifySet = Collections.EMPTY_SET;
	public ExplorerTreeNode(Object userObj,ExplorerTreeModel m) {
		super(userObj);
		this.model = m;
		if (userObj instanceof Diagram)((Diagram) userObj).addPropertyChangeListener(this);
	}
	@Override public boolean isLeaf() {
		if (!expanded) {
			model.updateChildren(new TreePath(model.getPathToRoot(this)));
			expanded = true;
		}
		return super.isLeaf();
	}
	boolean getPending() {
		return pending;
	}
	void setPending(boolean value) {
		pending = value;
	}
	public void setModifySet(Set set) {
		if (set == null||set.size() == 0)modifySet = Collections.EMPTY_SET;else modifySet = set;
	}
	public void nodeModified(Object node) {
		if (modifySet.contains(node)) {
			model.getNodeUpdater().schedule(this);
		}
		if (node == getUserObject()) {
			model.nodeChanged(this);
		}
	}
	public void remove() {
		this.userObject = null;
		if (children != null) {
			Iterator childrenIt = children.iterator();
			while (childrenIt.hasNext()) {
				((ExplorerTreeNode) childrenIt.next()).remove();
			}
			children.clear();
			children = null;
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource()instanceof Diagram) {
			if ("name".equals(evt.getPropertyName())) {
				model.nodeChanged(this);
			}
			if ("namespace".equals(evt.getPropertyName())) {
			}
		}
	}
}



