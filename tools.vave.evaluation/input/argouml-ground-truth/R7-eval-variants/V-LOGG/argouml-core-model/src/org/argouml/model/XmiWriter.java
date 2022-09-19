package org.argouml.model;


public interface XmiWriter {
	void write()throws UmlException;
	void setXmiExtensionWriter(XmiExtensionWriter xmiExtensionWriter);
}



