package org.argouml.util;

import java.util.Enumeration;
import java.util.Iterator;


public class EnumerationIterator implements Iterator {
	private Enumeration enumeration;
	public EnumerationIterator(Enumeration e) {
		enumeration = e;
	}
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}
	public Object next() {
		return enumeration.nextElement();
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}
}



