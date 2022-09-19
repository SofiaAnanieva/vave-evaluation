package org.argouml.uml.diagram.activity.ui;

import java.awt.Rectangle;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;


public class FigCallState extends FigActionState {
	@SuppressWarnings("deprecation")@Deprecated public FigCallState() {
		super();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigCallState(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
	}
	public FigCallState(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
	}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_CALLSTATE;
	}
	@Override public Object clone() {
		FigCallState figClone = (FigCallState) super.clone();
		return figClone;
	}
	@Override public Selection makeSelection() {
		return new SelectionCallState(this);
	}
}



