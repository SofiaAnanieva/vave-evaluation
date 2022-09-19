package org.argouml.uml.util.namespace;


public class StringNamespaceElement implements NamespaceElement {
	private final String element;
	public StringNamespaceElement(String strelement) {
		this.element = strelement;
	}
	public Object getNamespaceElement() {
		return element;
	}
	public String toString() {
		return element;
	}
}



