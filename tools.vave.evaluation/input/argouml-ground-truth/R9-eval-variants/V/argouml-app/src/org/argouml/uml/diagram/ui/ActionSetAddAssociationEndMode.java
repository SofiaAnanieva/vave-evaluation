package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;


public class ActionSetAddAssociationEndMode extends ActionSetMode {
	private static final long serialVersionUID = 2908695768709766241l;
	public ActionSetAddAssociationEndMode(String name) {
		super(ModeCreateAssociationEnd.class,"edgeClass",Model.getMetaTypes().getAssociationEnd(),name);
	}
}



