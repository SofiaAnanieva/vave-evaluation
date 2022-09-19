package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class InheritanceNode implements WeakExplorerNode {
	private Object parent;
	public InheritanceNode(Object p) {
		parent = p;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Inheritance";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof InheritanceNode;
	}
}



