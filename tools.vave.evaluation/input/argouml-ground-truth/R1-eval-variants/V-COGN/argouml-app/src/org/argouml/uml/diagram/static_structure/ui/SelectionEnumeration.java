package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;


class SelectionEnumeration extends SelectionDataType {
	private static String[]instructions =  {"Add a super-enumeration","Add a sub-enumeration",null,null,null,"Move object(s)"};
	public SelectionEnumeration(Fig f) {
		super(f);
	}
	@Override protected String getInstructions(int i) {
		return instructions[i - 10];
	}
	@Override protected Object getNewNode(int index) {
		Object ns = Model.getFacade().getNamespace(getContent().getOwner());
		return Model.getCoreFactory().buildEnumeration("",ns);
	}
	@Override protected Object getNewNodeType(int index) {
		return Model.getMetaTypes().getEnumeration();
	}
}



