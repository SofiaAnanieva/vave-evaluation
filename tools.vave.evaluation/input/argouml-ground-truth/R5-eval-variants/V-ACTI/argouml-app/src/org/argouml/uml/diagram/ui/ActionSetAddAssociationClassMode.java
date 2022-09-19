package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;


public class ActionSetAddAssociationClassMode extends ActionSetMode {
	private static final long serialVersionUID = -884044085661992872l;
	public ActionSetAddAssociationClassMode(String name) {
		super(ModeCreateAssociationClass.class,"edgeClass",Model.getMetaTypes().getAssociationClass(),name);
	}
}



