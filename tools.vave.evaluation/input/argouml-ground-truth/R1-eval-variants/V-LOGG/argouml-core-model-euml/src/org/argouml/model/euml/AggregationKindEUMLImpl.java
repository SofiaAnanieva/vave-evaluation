package org.argouml.model.euml;

import org.argouml.model.AggregationKind;


class AggregationKindEUMLImpl implements AggregationKind {
	public Object getAggregate() {
		return org.eclipse.uml2.uml.AggregationKind.SHARED_LITERAL;
	}
	public Object getComposite() {
		return org.eclipse.uml2.uml.AggregationKind.COMPOSITE_LITERAL;
	}
	public Object getNone() {
		return org.eclipse.uml2.uml.AggregationKind.NONE_LITERAL;
	}
}



