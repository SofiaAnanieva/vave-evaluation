package org.argouml.model.euml;

import org.argouml.model.ConcurrencyKind;
import org.eclipse.uml2.uml.CallConcurrencyKind;


class ConcurrencyKindEUMLImpl implements ConcurrencyKind {
	public Object getConcurrent() {
		return CallConcurrencyKind.CONCURRENT_LITERAL;
	}
	public Object getGuarded() {
		return CallConcurrencyKind.GUARDED_LITERAL;
	}
	public Object getSequential() {
		return CallConcurrencyKind.SEQUENTIAL_LITERAL;
	}
}



