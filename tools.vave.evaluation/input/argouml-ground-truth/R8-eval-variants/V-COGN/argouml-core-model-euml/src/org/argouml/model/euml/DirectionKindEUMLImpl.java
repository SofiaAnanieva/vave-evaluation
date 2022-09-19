package org.argouml.model.euml;

import org.argouml.model.DirectionKind;
import org.eclipse.uml2.uml.ParameterDirectionKind;


class DirectionKindEUMLImpl implements DirectionKind {
	public Object getInOutParameter() {
		return ParameterDirectionKind.INOUT_LITERAL;
	}
	public Object getInParameter() {
		return ParameterDirectionKind.IN_LITERAL;
	}
	public Object getOutParameter() {
		return ParameterDirectionKind.OUT_LITERAL;
	}
	public Object getReturnParameter() {
		return ParameterDirectionKind.RETURN_LITERAL;
	}
}



