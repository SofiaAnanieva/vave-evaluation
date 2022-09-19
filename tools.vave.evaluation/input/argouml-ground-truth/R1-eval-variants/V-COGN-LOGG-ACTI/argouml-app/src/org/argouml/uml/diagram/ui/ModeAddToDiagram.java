package org.argouml.uml.diagram.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;
import org.argouml.uml.diagram.ui.UMLDiagram;


public class ModeAddToDiagram extends FigModifyingModeImpl {
	private static final long serialVersionUID = 8861862975789222877l;
	private final Collection<Object>modelElements;
	private final boolean addRelatedEdges = true;
	private final String instructions;
	private static final Logger LOG = Logger.getLogger(ModeAddToDiagram.class);
	public ModeAddToDiagram(final Collection<Object>modelElements,final String instructions) {
		this.modelElements = modelElements;
		if (instructions == null) {
			this.instructions = "";
		}else {
			this.instructions = instructions;
		}
	}
	@Override public String instructions() {
		return instructions;
	}
	public Cursor getInitialCursor() {
		return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	}
	@Override public void mouseReleased(final MouseEvent me) {
		if (me.isConsumed()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("MouseReleased but rejected as already consumed");
			}
			return;
		}
		UndoManager.getInstance().addMementoLock(this);
		start();
		MutableGraphModel gm = (MutableGraphModel) editor.getGraphModel();
		final int x = me.getX();
		final int y = me.getY();
		editor.damageAll();
		final Point snapPt = new Point(x,y);
		editor.snap(snapPt);
		editor.damageAll();
		int count = 0;
		Layer lay = editor.getLayerManager().getActiveLayer();
		GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
		final List<FigNode>placedFigs = new ArrayList<FigNode>(modelElements.size());
		ArgoDiagram diag = DiagramUtils.getActiveDiagram();
		if (diag instanceof UMLDiagram) {
			for (final Object node:modelElements) {
				if (((UMLDiagram) diag).doesAccept(node)) {
					final FigNode pers = renderer.getFigNodeFor(gm,lay,node,null);
					pers.setLocation(snapPt.x + (count++ * 100),snapPt.y);
					if (LOG.isDebugEnabled()) {
						LOG.debug("mouseMoved: Location set (" + pers.getX() + "," + pers.getY() + ")");
					}
					UndoManager.getInstance().startChain();
					editor.add(pers);
					gm.addNode(node);
					if (addRelatedEdges) {
						gm.addNodeRelatedEdges(node);
					}
					Fig encloser = null;
					final Rectangle bbox = pers.getBounds();
					final List<Fig>otherFigs = lay.getContents();
					for (final Fig otherFig:otherFigs) {
						if (!(otherFig.getUseTrapRect())) {
							continue;
						}
						if (!(otherFig instanceof FigNode)) {
							continue;
						}
						if (!otherFig.isVisible()) {
							continue;
						}
						if (otherFig.equals(pers)) {
							continue;
						}
						final Rectangle trap = otherFig.getTrapRect();
						if (trap != null&&trap.contains(bbox.x,bbox.y)&&trap.contains(bbox.x + bbox.width,bbox.y + bbox.height)) {
							encloser = otherFig;
						}
					}
					pers.setEnclosingFig(encloser);
					placedFigs.add(pers);
				}
			}
		}
		UndoManager.getInstance().removeMementoLock(this);
		if (UndoManager.getInstance().isGenerateMementos()) {
			AddToDiagramMemento memento = new AddToDiagramMemento(editor,placedFigs);
			UndoManager.getInstance().addMemento(memento);
		}
		UndoManager.getInstance().addMementoLock(this);
		editor.getSelectionManager().select(placedFigs);
		done();
		me.consume();
	}
	public void keyTyped(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
			LOG.debug("ESC pressed");
			leave();
		}
	}
}

class AddToDiagramMemento extends Memento {
	private final List<FigNode>nodesPlaced;
	private final Editor editor;
	private final MutableGraphModel mgm;
	AddToDiagramMemento(final Editor ed,final List<FigNode>nodesPlaced) {
			this.nodesPlaced = nodesPlaced;
			this.editor = ed;
			this.mgm = (MutableGraphModel) editor.getGraphModel();
		}
	public void undo() {
		UndoManager.getInstance().addMementoLock(this);
		for (FigNode figNode:nodesPlaced) {
			mgm.removeNode(figNode.getOwner());
			editor.remove(figNode);
		}
		UndoManager.getInstance().removeMementoLock(this);
	}
	public void redo() {
		UndoManager.getInstance().addMementoLock(this);
		for (FigNode figNode:nodesPlaced) {
			editor.add(figNode);
			mgm.addNode(figNode.getOwner());
		}
		UndoManager.getInstance().removeMementoLock(this);
	}
	public void dispose() {
	}
	public String toString() {
		return(isStartChain()?"*":" ") + "AddToDiagramMemento";
	}
}



