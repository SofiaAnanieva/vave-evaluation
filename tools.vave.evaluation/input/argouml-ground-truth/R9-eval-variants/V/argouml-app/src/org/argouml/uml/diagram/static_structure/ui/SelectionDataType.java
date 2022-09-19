package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


class SelectionDataType extends SelectionGeneralizableElement {
	public SelectionDataType(Fig f) {
		super(f);
	}
	protected Object getNewNode(int buttonCode) {
		Object ns = Model.getFacade().getNamespace(getContent().getOwner());
		return Model.getCoreFactory().buildDataType("",ns);
	}
	protected Object getNewNodeType(int buttonCode) {
		return Model.getMetaTypes().getDataType();
	}
}



