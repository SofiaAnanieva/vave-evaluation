package org.argouml.uml;

import java.util.Iterator;
import org.argouml.util.ChildGenerator;


public class GenCompositeClasses2 extends GenCompositeClasses implements ChildGenerator {
	private static final GenCompositeClasses2 SINGLETON = new GenCompositeClasses2();
	public static GenCompositeClasses2 getInstance() {
		return SINGLETON;
	}
	public Iterator childIterator(Object parent) {
		return collectChildren(parent).iterator();
	}
}



