package org.argouml.util;


public class PredicateEquals implements Predicate {
	private Object pattern;
	public PredicateEquals(Object patternObject) {
		pattern = patternObject;
	}
	public boolean evaluate(Object object) {
		if (pattern == null) {
			return object == null;
		}
		return pattern.equals(object);
	}
}



