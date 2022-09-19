package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class OutgoingDependencyNode implements WeakExplorerNode {
	private Object parent;
	public OutgoingDependencyNode(Object p) {
		parent = p;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Outgoing Dependencies";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof OutgoingDependencyNode;
	}
}



