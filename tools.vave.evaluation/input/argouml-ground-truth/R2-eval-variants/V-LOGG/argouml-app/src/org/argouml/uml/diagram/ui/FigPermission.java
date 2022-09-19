package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;


public class FigPermission extends FigDependency {
	@SuppressWarnings("deprecation")@Deprecated public FigPermission() {
		super();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigPermission(Object edge) {
		super(edge);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigPermission(Object edge,Layer lay) {
		super(edge,lay);
	}
	public FigPermission(Object owner,DiagramSettings settings) {
		super(owner,settings);
	}
}



