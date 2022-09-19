package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;
import org.tigris.gef.base.ModeCreatePolyEdge;


public class ActionSetAddAssociationMode extends ActionSetMode {
	private static final long serialVersionUID = -3869448253653259670l;
	public ActionSetAddAssociationMode(Object aggregationKind,boolean unidirectional,String name) {
		super(ModeCreatePolyEdge.class,"edgeClass",Model.getMetaTypes().getAssociation(),name);
		modeArgs.put("aggregation",aggregationKind);
		modeArgs.put("unidirectional",Boolean.valueOf(unidirectional));
	}
}



