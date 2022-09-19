package org.argouml.uml.diagram.state.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.SelectionActionState;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;


public abstract class FigStateVertex extends FigNodeModelElement {
	@SuppressWarnings("deprecation")@Deprecated public FigStateVertex() {
		this.allowRemoveFromDiagram(false);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigStateVertex(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
	}
	public FigStateVertex(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		this.allowRemoveFromDiagram(false);
	}
	@Override public void setEnclosingFig(Fig encloser) {
		LayerPerspective layer = (LayerPerspective) getLayer();
		if (layer == null) {
			return;
		}
		super.setEnclosingFig(encloser);
		if (!(Model.getFacade().isAStateVertex(getOwner()))) {
			return;
		}
		Object stateVertex = getOwner();
		Object compositeState = null;
		if (encloser != null&&(Model.getFacade().isACompositeState(encloser.getOwner()))) {
			compositeState = encloser.getOwner();
			((FigStateVertex) encloser).redrawEnclosedFigs();
		}else {
			compositeState = Model.getStateMachinesHelper().getTop(Model.getStateMachinesHelper().getStateMachine(stateVertex));
		}
		if (compositeState != null) {
			if (Model.getFacade().getContainer(stateVertex) != compositeState) {
				Model.getStateMachinesHelper().setContainer(stateVertex,compositeState);
			}
		}
	}
	public void redrawEnclosedFigs() {
		Editor editor = Globals.curEditor();
		if (editor != null&&!getEnclosedFigs().isEmpty()) {
			LayerDiagram lay = ((LayerDiagram) editor.getLayerManager().getActiveLayer());
			for (Fig f:getEnclosedFigs()) {
				lay.bringInFrontOf(f,this);
				if (f instanceof FigNode) {
					FigNode fn = (FigNode) f;
					Iterator it = fn.getFigEdges().iterator();
					while (it.hasNext()) {
						lay.bringInFrontOf(((FigEdge) it.next()),this);
					}
					if (fn instanceof FigStateVertex) {
						((FigStateVertex) fn).redrawEnclosedFigs();
					}
				}
			}
		}
	}
	@Override public Selection makeSelection() {
		Object pstate = getOwner();
		if (pstate != null) {
			if (Model.getFacade().isAActivityGraph(Model.getFacade().getStateMachine(Model.getFacade().getContainer(pstate)))) {
				return new SelectionActionState(this);
			}
		}
		return null;
	}
	private static final int CIRCLE_POINTS = 32;
	List<Point>getCircleGravityPoints() {
		List<Point>ret = new ArrayList<Point>();
		int cx = getBigPort().getCenter().x;
		int cy = getBigPort().getCenter().y;
		double radius = getBigPort().getWidth() / 2 + 1;
		final double pi2 = Math.PI * 2;
		for (int i = 0;i < CIRCLE_POINTS;i++) {
			int x = (int) (cx + Math.cos(pi2 * i / CIRCLE_POINTS) * radius);
			int y = (int) (cy + Math.sin(pi2 * i / CIRCLE_POINTS) * radius);
			ret.add(new Point(x,y));
		}
		return ret;
	}
}



