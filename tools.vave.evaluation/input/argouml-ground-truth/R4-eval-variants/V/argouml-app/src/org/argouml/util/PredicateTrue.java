package org.argouml.util;


public class PredicateTrue implements Predicate {
	private PredicateTrue() {
	}
	public boolean evaluate(Object obj) {
		return true;
	}
	private static PredicateTrue theInstance = new PredicateTrue();
	public static PredicateTrue getInstance() {
		return theInstance;
	}
}



