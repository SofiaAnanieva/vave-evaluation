package org.argouml.uml.util.namespace;

import java.util.Iterator;


public interface Namespace {
	public static final String JAVA_NS_TOKEN = ".";
	public static final String UML_NS_TOKEN = "::";
	public static final String CPP_NS_TOKEN = "::";
	Namespace getCommonNamespace(Namespace namespace);
	Namespace getBaseNamespace();
	void pushNamespaceElement(NamespaceElement element);
	NamespaceElement popNamespaceElement();
	NamespaceElement peekNamespaceElement();
	void setDefaultScopeToken(String token);
	Iterator iterator();
	boolean isEmpty();
	String toString(String token);
}



