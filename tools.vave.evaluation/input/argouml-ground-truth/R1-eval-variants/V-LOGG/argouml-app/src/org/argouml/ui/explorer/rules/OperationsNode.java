package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class OperationsNode implements WeakExplorerNode {
	private Object parent;
	public OperationsNode(Object p) {
		parent = p;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Operations";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof OperationsNode;
	}
}



