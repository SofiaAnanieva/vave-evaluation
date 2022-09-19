package org.argouml.uml.ui.behavior.state_machines;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;


public class UMLCompositeStateSubvertexListModel extends UMLModelElementListModel2 {
	public UMLCompositeStateSubvertexListModel() {
		super("subvertex");
	}
	protected void buildModelList() {
		setAllElements(Model.getFacade().getSubvertices(getTarget()));
	}
	protected boolean isValidElement(Object element) {
		return Model.getFacade().getSubvertices(getTarget()).contains(element);
	}
}



