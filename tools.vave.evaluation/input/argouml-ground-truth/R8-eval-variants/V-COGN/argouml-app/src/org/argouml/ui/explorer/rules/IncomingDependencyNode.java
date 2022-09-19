package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class IncomingDependencyNode implements WeakExplorerNode {
	private Object parent;
	public IncomingDependencyNode(Object theParent) {
		this.parent = theParent;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Incoming Dependencies";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof IncomingDependencyNode;
	}
}



