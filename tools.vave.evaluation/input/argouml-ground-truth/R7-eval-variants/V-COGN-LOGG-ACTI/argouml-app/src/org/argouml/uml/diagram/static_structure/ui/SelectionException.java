package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


class SelectionException extends SelectionGeneralizableElement {
	public SelectionException(Fig f) {
		super(f);
	}
	@Override protected Object getNewNode(int buttonCode) {
		return Model.getCommonBehaviorFactory().createException();
	}
	@Override protected Object getNewNodeType(int buttonCode) {
		return Model.getMetaTypes().getException();
	}
}



