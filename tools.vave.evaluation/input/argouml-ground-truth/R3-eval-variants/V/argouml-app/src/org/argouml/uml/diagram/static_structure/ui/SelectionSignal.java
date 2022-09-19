package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


class SelectionSignal extends SelectionGeneralizableElement {
	public SelectionSignal(Fig f) {
		super(f);
	}
	protected Object getNewNode(int index) {
		return Model.getCommonBehaviorFactory().createSignal();
	}
	protected Object getNewNodeType(int index) {
		return Model.getMetaTypes().getSignal();
	}
}



