package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Layer;


public class FigUsage extends FigDependency {
	@SuppressWarnings("deprecation")@Deprecated public FigUsage() {
		super();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigUsage(Object edge) {
		super(edge);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigUsage(Object edge,Layer lay) {
		super(edge,lay);
	}
	public FigUsage(Object owner,DiagramSettings settings) {
		super(owner,settings);
	}
	private static final long serialVersionUID = -1805275467987372774l;
}



