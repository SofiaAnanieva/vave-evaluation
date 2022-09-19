package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;


public class AttributesNode implements WeakExplorerNode {
	private Object parent;
	public AttributesNode(Object theParent) {
		this.parent = theParent;
	}
	public Object getParent() {
		return parent;
	}
	public String toString() {
		return"Attributes";
	}
	public boolean subsumes(Object obj) {
		return obj instanceof AttributesNode;
	}
}



