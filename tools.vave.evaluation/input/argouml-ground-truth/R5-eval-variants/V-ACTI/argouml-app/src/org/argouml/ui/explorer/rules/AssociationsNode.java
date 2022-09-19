package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class AssociationsNode implements WeakExplorerNode {
	private Object parent;
	public AssociationsNode(Object theParent) {
		this.parent = theParent;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Associations";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof AssociationsNode;
	}
}



