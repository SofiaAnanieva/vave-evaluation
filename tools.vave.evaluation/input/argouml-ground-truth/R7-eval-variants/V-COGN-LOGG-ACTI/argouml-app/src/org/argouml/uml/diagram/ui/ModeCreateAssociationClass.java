package org.argouml.uml.diagram.ui;

import org.apache.log4j.Logger;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.FigEdge;


public class ModeCreateAssociationClass extends ModeCreatePolyEdge {
	private static final long serialVersionUID = -8656139458297932182l;
	private static final Logger LOG = Logger.getLogger(ModeCreateAssociationClass.class);
	private static final int DISTANCE = 80;
	@Override protected void endAttached(FigEdge fe) {
	}
	public static void buildInActiveLayer(Editor editor,Object element) {
	}
	private static void buildParts(Editor editor,FigAssociationClass thisFig,Layer lay) {
	}
}



