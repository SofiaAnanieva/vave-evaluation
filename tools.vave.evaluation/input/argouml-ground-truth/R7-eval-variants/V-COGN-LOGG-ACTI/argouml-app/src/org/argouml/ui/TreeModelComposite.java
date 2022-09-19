package org.argouml.ui;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;


public class TreeModelComposite extends TreeModelSupport implements TreeModel {
	private static final Logger LOG = Logger.getLogger(TreeModelComposite.class);
	private Object root;
	public TreeModelComposite(String name) {
		super(name);
	}
	public Object getRoot() {
		return root;
	}
	public Object getChild(Object parent,int index) {
		for (TreeModel tm:getGoRuleList()) {
			int childCount = tm.getChildCount(parent);
			if (index < childCount) {
				return tm.getChild(parent,index);
			}
			index -= childCount;
		}
		return null;
	}
	public int getChildCount(Object parent) {
		int childCount = 0;
		for (TreeModel tm:getGoRuleList()) {
			childCount += tm.getChildCount(parent);
		}
		return childCount;
	}
	public int getIndexOfChild(Object parent,Object child) {
		int childCount = 0;
		for (TreeModel tm:getGoRuleList()) {
			int childIndex = tm.getIndexOfChild(parent,child);
			if (childIndex != -1) {
				return childIndex + childCount;
			}
			childCount += tm.getChildCount(parent);
		}
		LOG.debug("child not found!");
		return-1;
	}
	public boolean isLeaf(Object node) {
		for (TreeModel tm:getGoRuleList()) {
			if (!tm.isLeaf(node)) {
				return false;
			}
		}
		return true;
	}
	public void valueForPathChanged(TreePath path,Object newValue) {
	}
	public void setRoot(Object r) {
		root = r;
	}
}



