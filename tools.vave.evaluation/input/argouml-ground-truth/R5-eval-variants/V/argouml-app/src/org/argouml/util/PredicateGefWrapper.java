package org.argouml.util;


@Deprecated public class PredicateGefWrapper implements Predicate {
	private org.tigris.gef.util.Predicate predicate;
	public PredicateGefWrapper(org.tigris.gef.util.Predicate gefPredicate) {
		predicate = gefPredicate;
	}
	public boolean evaluate(Object object) {
		return predicate.predicate(object);
	}
	public org.tigris.gef.util.Predicate getGefPredicate() {
		return predicate;
	}
}



