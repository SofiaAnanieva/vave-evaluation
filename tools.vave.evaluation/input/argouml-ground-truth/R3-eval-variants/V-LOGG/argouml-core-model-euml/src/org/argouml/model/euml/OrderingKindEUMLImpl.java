package org.argouml.model.euml;

import org.argouml.model.OrderingKind;


class OrderingKindEUMLImpl implements OrderingKind {
	static final String ORDERED = "ordered";
	public Object getOrdered() {
		return ORDERED;
	}
	public Object getUnordered() {
		return"unordered";
	}
}



