package org.argouml.util;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SingleElementIterator<T>implements Iterator {
	private boolean done = false;
	private T target;
	public SingleElementIterator(T o) {
		target = o;
	}
	public boolean hasNext() {
		if (!done) {
			return true;
		}
		return false;
	}
	public T next() {
		if (!done) {
			done = true;
			return target;
		}
		throw new NoSuchElementException();
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}
}



