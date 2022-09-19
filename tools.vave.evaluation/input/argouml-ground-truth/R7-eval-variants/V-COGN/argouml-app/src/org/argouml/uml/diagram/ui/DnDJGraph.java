package org.argouml.uml.diagram.ui;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.argouml.ui.TransferableModelElements;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.ConnectionConstrainer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.FigNode;
import org.argouml.uml.diagram.ui.UMLDiagram;


class DnDJGraph extends JGraph implements DropTargetListener {
	public DnDJGraph() {
		super();
		makeDropTarget();
	}
	public DnDJGraph(ConnectionConstrainer cc) {
		super(cc);
		makeDropTarget();
	}
	public DnDJGraph(Diagram d) {
		super(d);
		makeDropTarget();
	}
	public DnDJGraph(GraphModel gm) {
		super(gm);
		makeDropTarget();
	}
	public DnDJGraph(Editor ed) {
		super(ed);
		makeDropTarget();
	}
	private void makeDropTarget() {
		new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE,this);
	}
	public void dragEnter(DropTargetDragEvent dtde) {
		try {
			if (dtde.isDataFlavorSupported(TransferableModelElements.UML_COLLECTION_FLAVOR)) {
				dtde.acceptDrag(dtde.getDropAction());
				return;
			}
		}catch (NullPointerException e) {
		}
		dtde.rejectDrag();
	}
	public void dragOver(DropTargetDragEvent dtde) {
		try {
			ArgoDiagram dia = DiagramUtils.getActiveDiagram();
			if (dia instanceof UMLDiagram) {
				dtde.acceptDrag(dtde.getDropAction());
				return;
			}
			if (dtde.isDataFlavorSupported(TransferableModelElements.UML_COLLECTION_FLAVOR)) {
				dtde.acceptDrag(dtde.getDropAction());
				return;
			}
		}catch (NullPointerException e) {
		}
		dtde.rejectDrag();
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}
	public void dragExit(DropTargetEvent dte) {
	}
	public void drop(DropTargetDropEvent dropTargetDropEvent) {
		Transferable tr = dropTargetDropEvent.getTransferable();
		if (!tr.isDataFlavorSupported(TransferableModelElements.UML_COLLECTION_FLAVOR)) {
			dropTargetDropEvent.rejectDrop();
			return;
		}
		dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
		Collection modelElements;
		try {
			ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
			modelElements = (Collection) tr.getTransferData(TransferableModelElements.UML_COLLECTION_FLAVOR);
			Iterator i = modelElements.iterator();
			while (i.hasNext()) {
				FigNode figNode = ((UMLDiagram) diagram).drop(i.next(),dropTargetDropEvent.getLocation());
				if (figNode != null) {
					MutableGraphModel gm = (MutableGraphModel) diagram.getGraphModel();
					if (!gm.getNodes().contains(figNode.getOwner())) {
						gm.getNodes().add(figNode.getOwner());
					}
					Globals.curEditor().getLayerManager().getActiveLayer().add(figNode);
					gm.addNodeRelatedEdges(figNode.getOwner());
				}
			}
			dropTargetDropEvent.getDropTargetContext().dropComplete(true);
		}catch (UnsupportedFlavorException e) {
		}catch (IOException e) {
		}
	}
	private static final long serialVersionUID = -5753683239435014182l;
}



