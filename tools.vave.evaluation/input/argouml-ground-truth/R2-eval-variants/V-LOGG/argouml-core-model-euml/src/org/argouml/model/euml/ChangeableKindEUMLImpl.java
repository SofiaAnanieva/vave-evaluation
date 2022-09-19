package org.argouml.model.euml;

import org.argouml.model.ChangeableKind;


class ChangeableKindEUMLImpl implements ChangeableKind {
	public Object getAddOnly() {
		return"";
	}
	public Object getChangeable() {
		return"changeable";
	}
	public Object getFrozen() {
		return"frozen";
	}
}



